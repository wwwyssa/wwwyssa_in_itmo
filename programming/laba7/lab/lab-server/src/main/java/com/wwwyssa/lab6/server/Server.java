package com.wwwyssa.lab6.server;

import com.wwwyssa.lab6.common.util.Pair;
import com.wwwyssa.lab6.common.util.Request;
import com.wwwyssa.lab6.common.util.Response;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.ArgumentValidator;
import com.wwwyssa.lab6.server.commands.*;
import com.wwwyssa.lab6.server.commands.Show;
import com.wwwyssa.lab6.server.managers.CollectionManager;
import com.wwwyssa.lab6.server.managers.CommandManager;
import com.wwwyssa.lab6.server.managers.Runner;
import com.wwwyssa.lab6.server.managers.ServerConnectionManager;
import com.wwwyssa.lab6.server.managers.dbManagers.DDLManager;
import com.wwwyssa.lab6.server.managers.dbManagers.UserDatabaseManager;
import com.wwwyssa.lab6.server.util.AskingCommand;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.concurrent.*;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


/**
* Класс Server - основной класс сервера, который отвечает за запуск и управление сервером.
*/

public class Server {

/**
    * Логгер для сервера
 */
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



private static final int PORT = 15719;
private static CommandManager commandManager;
private static ServerConnectionManager networkManager;
private static Selector selector;

private static final CollectionManager collectionManager = CollectionManager.getInstance();
private static Runner runner;
private static final Map<SocketChannel, CompletableFuture<Response>> responseFutures = new ConcurrentHashMap<>();

private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
private static final ExecutorService requestProcessingPool = Executors.newCachedThreadPool();


private static volatile boolean isRunning = true;
public static void main(String[] args) throws SQLException {


    networkManager = new ServerConnectionManager(PORT);
    Server.logger.info("Server started");
   /*try {

        DDLManager ddlManager = new DDLManager("jdbc:postgresql://pg:5432/studs", "s466650", "");
        ddlManager.dropTables();
        System.out.println("Таблицы удалены");
        ddlManager.createTables();
        System.out.println("Таблицы созданы");
    } catch (SQLException e) {
        Server.logger.info("Ошибка при работе с таблицами: " + e.getMessage());
        throw new RuntimeException(e);
    }*/

    ExecutionResponse loadStatus = collectionManager.loadCollection();
    // Проверка успешности загрузки коллекции
    if (!loadStatus.getExitCode()) {
        logger.severe(loadStatus.getAnswer().getAnswer().toString());
        System.exit(1);
    }
    logger.info("The collection file has been successfully loaded!");
    // Регистрация команд
    commandManager = new CommandManager() {{
        register("help", new Help(this));
        register("show",  new Show(CollectionManager.getInstance()));
        register("info",  new Info(CollectionManager.getInstance()));
        register("averageOfManufactureCost", new AverageOfManufactureCost(CollectionManager.getInstance()));
        register("minByName", new MinByName(CollectionManager.getInstance()));
        register("clear", new Clear(CollectionManager.getInstance()));
        register("printFieldAscendingPartNumber",  new PrintFieldAscendingPartNumber(CollectionManager.getInstance()));
        register("removeGreaterKey",  new RemoveGreaterKey(CollectionManager.getInstance()));
        register("add",  new Add(CollectionManager.getInstance()));
        register("removeById",  new RemoveById(CollectionManager.getInstance()));
        register("removeGreater",  new RemoveGreater(CollectionManager.getInstance()));
        register("executeScript", new VoidCommand("execute_script", "исполнить скрипт из файла", this));
        register("exit", new VoidCommand("exit", "завершить программу", this));
    }};
    runner = new Runner(commandManager);

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

    run();
}

/**
    * Метод run - запускает сервер и обрабатывает входящие соединения.
 */
public static void run() {
    try {
        // Создание селектора для обработки нескольких каналов
        selector = Selector.open();

        // Создание и настройка серверного канала
        networkManager.startServer();

        // Регистрация серверного канала в селекторе для обработки входящих соединений
        networkManager.getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);

        logger.info("Selector started");
        logger.info("To stop the server, press [Ctrl + C]");
        ExecutorService threadPool = Executors.newCachedThreadPool();
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
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                            SocketChannel clientChannel = serverSocketChannel.accept();
                            logger.info("Client connected: " + clientChannel.getRemoteAddress());

                            // Настройка канала для неблокирующего режима
                            clientChannel.configureBlocking(false);


                            threadPool.submit(() -> {
                                try {
                                    initialCommandsData(clientChannel, key); // отправка клиенту списка команд
                                } catch (ClosedChannelException e) {
                                    throw new RuntimeException(e);
                                }
                            });


                            clientChannel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            clientChannel.configureBlocking(false);
                            if (responseFutures.get(clientChannel) != null) {
                                continue; // Пропускаем итерацию, если уже идёт обработка этого клиента
                            }
                            CompletableFuture<Response> responseFuture = new CompletableFuture<>();
                            new Thread(() -> {
                                try {
                                    Request request = networkManager.receive(clientChannel, key);
                                    logger.info("Request received from client: " + request);
                                    // Создаем и запускаем поток для обработки запроса
                                    processRequestAsync(request, key, responseFuture);
                                } catch (ServerConnectionManager.NullRequestException | SocketException | NullPointerException e) {
                                    logger.severe("Error receiving request from client: " + e.getMessage());
                                    key.cancel();
                                } catch (IOException | ClassNotFoundException e) {
                                    logger.severe("Fatal error: " + e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            }).start();
                            
                            responseFutures.put(clientChannel, responseFuture);
                            clientChannel.register(selector, SelectionKey.OP_WRITE);
                        }  else if (key.isWritable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            clientChannel.configureBlocking(false);

                            cachedThreadPool.submit(() -> {
                                try {
                                    CompletableFuture<Response> responseFuture = responseFutures.get(clientChannel);
                                    if (responseFuture != null) {
                                        Response response = responseFuture.get();
                                        networkManager.send(response, clientChannel);
                                        logger.info("Response sent to client: " + clientChannel.getRemoteAddress());
                                        responseFutures.remove(clientChannel);
                                    } else {
                                        logger.severe("No response future found for client: " + clientChannel.getRemoteAddress());
                                    }
                                } catch (IOException e) {
                                    logger.severe("Error sending response to client: " + e.getMessage());
                                    key.cancel();
                                } catch (ExecutionException | InterruptedException e) {
                                    logger.severe("Error doing the command: " + e.getMessage());
                                    Thread.currentThread().interrupt(); // Восстанавливаем флаг прерывания
                                    throw new RuntimeException(e);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            clientChannel.register(selector, SelectionKey.OP_READ);
                        }
                    }
                } catch (SocketException | CancelledKeyException e) {
                    try (var channel = key.channel()) {
                        logger.severe("Client " + channel.toString() + " disconnected");
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
        logger.severe(e.getMessage());
        System.exit(1);
    } catch (IOException | NullPointerException e) {
        logger.severe("Error while running the server: " + e.getMessage());
    }
}


private static void processRequestAsync(Request request, SelectionKey key, CompletableFuture<Response> responseFuture) {
    requestProcessingPool.execute(() -> {
        try {
            Response response = processRequest(request);
            responseFuture.complete(response);
        } catch (ClosedChannelException e) {
            logger.severe("Error processing request: " + e.getMessage());
            responseFuture.completeExceptionally(e);
            key.cancel();
        } catch (SQLException e) {
            responseFuture.completeExceptionally(e);  // Важно завершить future
            logger.severe("SQL error processing request: " + e.getMessage());
        } catch (Exception e) {
            responseFuture.completeExceptionally(e);
            logger.severe("Unexpected error: " + e.getMessage());
        }
    });
}



private static Response processRequest(Request request) throws ClosedChannelException, SQLException {
    if (request.getCommand()[0].equals("register") || request.getCommand()[0].equals("login")) {
        ExecutionResponse authStatus = "register".equals(request.getCommand()[0])
                ? UserDatabaseManager.getInstance().addUser(request.getUser())
                : UserDatabaseManager.getInstance().checkUserPass(request.getUser().getName(), request.getUser().getPassword());
        if (authStatus.getExitCode()) {
            logger.info(authStatus.getAnswer() + " User: " + request.getUser().getName());
        } else {
            logger.warning(authStatus.getAnswer().getAnswer() + " User: " + request.getUser().getName());
        }
        return new Response(authStatus);
    }
    else {

        ExecutionResponse executionStatus = runner.launchCommand(request.getCommand(), request.getProduct(), request.getUser());
        if (!executionStatus.getExitCode()) {
            logger.severe(executionStatus.getAnswer().getAnswer().toString());
        } else {
            logger.info("Command executed successfully");
        }
        return new Response(executionStatus);
    }
}


/**
    * Метод InitialCommandsData - отправляет клиенту список доступных команд.
    * @param clientChannel - канал клиента, которому отправляется список команд.
    * @param key - ключ селектора для регистрации канала.
 */
private static synchronized void initialCommandsData(SocketChannel clientChannel, SelectionKey key) throws ClosedChannelException {
    try {
        Map<String, Pair<ArgumentValidator, Boolean>> commandsData = new HashMap<>();
        for (var entrySet : commandManager.getCommands().entrySet()) {
            boolean isAskingCommand = AskingCommand.class.isAssignableFrom(entrySet.getValue().getClass());
            commandsData.put(entrySet.getKey(), new Pair<>(entrySet.getValue().getArgumentValidator(), isAskingCommand));
        }
        networkManager.send(new Response(commandsData), clientChannel);
        logger.info("Command list sent to the client: " + clientChannel.getRemoteAddress());
    } catch (IOException e) {
        logger.severe("Error sending command list to the client: " + e.getMessage());
        key.cancel();
    } catch (NullPointerException e) {
        logger.severe("The client disconnected from the server: " + e.getMessage());
        key.cancel();
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    clientChannel.register(selector, SelectionKey.OP_READ);
}


}
