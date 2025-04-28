package com.wwwyssa.lab6.server;

import com.wwwyssa.lab6.common.util.Pair;
import com.wwwyssa.lab6.common.util.Request;
import com.wwwyssa.lab6.common.util.Response;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.ArgumentValidator;
import com.wwwyssa.lab6.server.commands.Command;
import com.wwwyssa.lab6.server.commands.Help;
import com.wwwyssa.lab6.server.commands.Show;
import com.wwwyssa.lab6.server.managers.CollectionManager;
import com.wwwyssa.lab6.server.managers.CommandManager;
import com.wwwyssa.lab6.server.managers.Runner;
import com.wwwyssa.lab6.server.managers.ServerConnectionManager;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Server {

    public static final Logger logger = Logger.getLogger(Server.class.getName());
    static { initLogger();}
    private static void initLogger() {
        try {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    String color = switch (record.getLevel().getName()) {
                        case "SEVERE" -> "\u001B[31m"; // Красный
                        case "WARNING" -> "\u001B[33m"; // Желтый
                        case "INFO" -> "\u001B[32m"; // Зеленый
                        default -> "\u001B[0m"; // Сброс цвета
                    };
                    return color + "[" + record.getLevel() + "] " +
                            "[" + Thread.currentThread().getName() + "] " +
                            "[" + new java.util.Date(record.getMillis()) + "] " +
                            formatMessage(record) + "\u001B[0m\n";
                }
            });
            // Настройка FileHandler
            FileHandler fileHandler = new FileHandler("server_logs.log", true); // true для добавления логов в конец файла
            fileHandler.setFormatter(new SimpleFormatter()); // Устанавливаем простой форматтер

            // Добавление обработчиков в логгер
            Server.logger.setUseParentHandlers(false);
            Server.logger.addHandler(consoleHandler);
            Server.logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to initialize file handler for logger: " + e.getMessage());
        }
    }



    private static final int PORT = 13876;
    private static CommandManager commandManager;
    private static ServerConnectionManager networkManager;
    private static Selector selector;
    private static Response response;
    private static final CollectionManager collectionManager = CollectionManager.getInstance();

    private static volatile boolean isRunning = true;
    public static void main(String[] args) {

        ExecutionResponse loadStatus = collectionManager.loadCollection();
        networkManager = new ServerConnectionManager(PORT);

        // Проверка успешности загрузки коллекции
        if (!loadStatus.getExitCode()) {
            logger.severe(loadStatus.getAnswer().getAnswer().toString());
            System.exit(1);
        }
        logger.info("The collection file has been successfully loaded!");
        // Регистрация команд
        commandManager = new CommandManager() {{
            register("Help", new Help(this));
            register("Show", new Show(CollectionManager.getInstance()));
        }};
        Runner runner = new Runner(commandManager);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                isRunning = false; // Останавливаем цикл
                collectionManager.saveCollection();
                selector.close();
                networkManager.close();
            } catch (Exception e) {
                logger.severe("An error occurred while shutting down the server: " + e.getMessage());
            }

        }));

        run(runner);
    }

    public static void run(Runner runner) {
        try {
            // Создание селектора для обработки нескольких каналов
            selector = Selector.open();

            // Создание и настройка серверного канала
            networkManager.startServer();

            // Регистрация серверного канала в селекторе для обработки входящих соединений
            networkManager.getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);

            logger.info("Selector started");
            logger.info("To stop the server, press [Ctrl + C]");
            while (isRunning) {

                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    logger.info("Processing key: " + key);
                    try {
                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                // Принимаем новое соединение
                                try (ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel()) {
                                    SocketChannel clientChannel = serverSocketChannel.accept();
                                    logger.info("Client connected: " + clientChannel.getRemoteAddress());

                                    // Настройка канала для неблокирующего режима
                                    clientChannel.configureBlocking(false);
                                    InitialCommandsData(clientChannel, key); //  отправка клиенту списка команд
                                }
                            } else if (key.isReadable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                Request request;
                                try {
                                    request = networkManager.receive(clientChannel, key);
                                } catch (ServerConnectionManager.NullRequestException | SocketException |
                                         NullPointerException e) {
                                    logger.severe("Error receiving request from client: " + e.getMessage());
                                    collectionManager.saveCollection();
                                    logger.info("Collection saved successfully");
                                    key.cancel();
                                    continue;
                                }
                                logger.info("Request received from client: " + request);
                                ExecutionResponse executionStatus = runner.launchCommand(request.getCommand(), request.getProduct());
                                response = new Response(executionStatus);
                                if (!executionStatus.getExitCode()) {
                                    logger.severe(executionStatus.getAnswer().getAnswer().toString());
                                } else {
                                    logger.info("Command executed successfully");
                                }
                                clientChannel.register(selector, SelectionKey.OP_WRITE);

                            } else if (key.isWritable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                try {
                                    networkManager.send(response, clientChannel);
                                    logger.info("Response sent to client: " + clientChannel.getRemoteAddress());
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                } catch (IOException e) {
                                    logger.severe("Error sending response to client: " + e.getMessage());
                                    collectionManager.saveCollection();
                                    logger.info("Collection saved successfully");
                                    key.cancel();
                                }
                            }
                        }
                    } catch (SocketException | CancelledKeyException e) {
                        try (var channel = key.channel()) {
                            logger.severe("Client " + channel.toString() + " disconnected");
                            collectionManager.saveCollection();
                            logger.info("Collection saved successfully");
                            key.cancel();
                        }
                    } finally {
                        keys.remove();
                    }
                }
            }
        } catch (ClosedSelectorException e) {
            logger.warning("Selector was closed.");
        } catch (EOFException e) {
            collectionManager.saveCollection();
            logger.info("Collection saved successfully");
            logger.severe(e.getMessage());
            System.exit(1);
        } catch (IOException | NullPointerException | ClassNotFoundException e) {
            logger.severe("Error while running the server: " + e.getMessage());
        }
    }


    private static void InitialCommandsData(SocketChannel clientChannel, SelectionKey key) throws ClosedChannelException {
        try {
            Map<String, Pair<ArgumentValidator, Boolean>> commandsData = new HashMap<>();
            for (var entrySet : commandManager.getCommands().entrySet()) {
                //boolean isAskingCommand = AskingCommand.class.isAssignableFrom(entrySet.getValue().getClass());
                boolean isAskingCommand = false;
                commandsData.put(entrySet.getKey(), new Pair<>(entrySet.getValue().getArgumentValidator(), isAskingCommand));
            }
            networkManager.send(new Response(commandsData), clientChannel);
            //logger.info("Command list sent to the client: " + clientChannel.getRemoteAddress());
        } catch (IOException e) {
            //logger.severe("Error sending command list to the client: " + e.getMessage());
            key.cancel();
        } catch (NullPointerException e) {
            //logger.severe("The client disconnected from the server: " + e.getMessage());
            key.cancel();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        clientChannel.register(selector, SelectionKey.OP_READ);
    }


}
