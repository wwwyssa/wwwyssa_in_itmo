package com.wwwyssa.lab6.client;


import com.wwwyssa.lab6.client.managers.AuthenticationManager;
import com.wwwyssa.lab6.client.managers.ConnectionManager;
import com.wwwyssa.lab6.client.util.*;
import com.wwwyssa.lab6.client.util.Console;
import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.common.util.User;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.util.Pair;
import com.wwwyssa.lab6.common.util.Request;
import com.wwwyssa.lab6.common.util.Response;
import com.wwwyssa.lab6.common.util.executions.ListAnswer;
import com.wwwyssa.lab6.common.validators.ArgumentValidator;
import com.wwwyssa.lab6.common.validators.NoArgumentsValidator;

import java.io.*;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;


/**
* Клиентский класс для подключения к серверу и отправки запросов.
* Обрабатывает ввод пользователя, проверку команд и сетевое взаимодействие.
*/
public final class Client {
    private static final Console console = new DefaultConsole();
    private static final int SERVER_PORT = 15719;
    private static final String SERVER_HOST = "localhost";
    private static Map<String, Pair<ArgumentValidator, Boolean>> commandsData;
    private static final ConnectionManager networkManager = new ConnectionManager(SERVER_PORT, SERVER_HOST);
    private static int scriptStackCounter = 0;
    private static int attempts = 1;
    private static User user;
    public static void main(String[] args) {
        do {
            try {
                networkManager.connect();
                commandsData = networkManager.receive().getCommandsMap();
                commandsData.put("execute_script", new Pair<>(new NoArgumentsValidator(), false));
                attempts = 1;
                console.println("Connected to " + SERVER_HOST + ":" + SERVER_PORT);
                if (attempts == 1 ){
                    user = AuthenticationManager.authenticateUser(networkManager, console);
                    console.println("Вы успешно авторизованы как " + user.getName());
                } else{
                    AuthenticationManager.sendAuthenticationRequest(networkManager, console, user, "login");
                    attempts = 1;
                }
                while (true) {
                    console.println("Введите команду:");
                    String inputCommand = console.input();
                    ExecutionResponse argumentStatus = validateCommand((inputCommand.trim() + " ").split(" ", 2));
                    if (!argumentStatus.getExitCode()) {
                        console.printError(argumentStatus.getAnswer().getAnswer().toString());
                    }
                    else {
                        Request request = prepareRequest(console, inputCommand);
                        if (request == null) {
                            continue; // Прерываем выполнение команды, если клиент не ввёл элемент коллекции
                        }

                        networkManager.send(request);
                        Response response = networkManager.receive();
                        if (response.getExecutionStatus().getExitCode()) {
                            if (response.getExecutionStatus().getAnswer() != null) {

                                if (response.getExecutionStatus().getAnswer().getClass() == ListAnswer.class){
                                    console.println(response.getExecutionStatus().getAnswer().getAnswer());
                                } else{
                                    console.println(response.getExecutionStatus().getAnswer());
                                }

                            }
                        } else {
                            console.printError(response.getExecutionStatus().getAnswer());
                        }
                    }
                }
            } catch (BufferOverflowException | BufferUnderflowException | IOException e) {
                console.printError("Не удалось подключиться к серверу. Проверьте, запущен ли сервер и доступен ли он по адресу " + SERVER_HOST + ":" + SERVER_PORT + " попытка " + attempts);
                console.println(e.getMessage());
                try {
                    Thread.sleep(2000);
                    attempts++;
                } catch (InterruptedException ignored) {}
            } catch (ClassNotFoundException e) {
                console.printError("Ошибка при работе с сервером: " + e.getMessage());
            }
        } while (attempts <= 5);
        console.printError("Превышено максимальное количество попыток подключения к серверу.");
    }


    /**
     * Запрашивает у пользователя ввод элемента коллекции.
     * Если ввод не прошёл валидацию, выводит сообщение об ошибке.
     *
     * @param console       Объект консоли для взаимодействия с пользователем.
     * @param inputCommand  Введённая пользователем команда.
     * @return Объект Request, содержащий команду и её аргументы.
     */
    private static Request askingRequest(Console console, String inputCommand) {
        ElementValidator elementValidator = new ElementValidator();
        Pair<ExecutionResponse, Product> validationStatusPair = elementValidator.validateAsking(console, Math.abs(new Random().nextLong()) + 1);
        if (!validationStatusPair.getFirst().getExitCode()) {
            console.printError(validationStatusPair.getFirst().getAnswer());
            return null;
        } else {
            return new Request(inputCommand, validationStatusPair.getSecond(), user);
        }
    }


    private static ExecutionResponse validateCommand(String[] userCommand) {
        try {
            if (userCommand[0].equals("exit")) {
                console.println("Завершение работы клиента...");
                try {
                    networkManager.close();
                } catch (IOException e) {
                    console.printError("Не удалось закрыть соединение с сервером.");
                }
                System.exit(0);
                return null;
            } else if (userCommand[0].equals("execute_script")) {
                System.out.println("EXECUTE_SCRIPT");
                return new ExecutionResponse(true, new AnswerString("Введена команда 'execute_script'. Валидация аргументов не требуется."));
            } else {
                var argumentValidator = commandsData.get(userCommand[0]);
                if (argumentValidator == null) {
                    return new ExecutionResponse(false,new AnswerString("Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'."));
                } else {
                    return argumentValidator.getFirst().validate(userCommand[1].trim(), userCommand[0]);
                }
            }
        } catch (NullPointerException e) {
            return new ExecutionResponse(false,new AnswerString("Введено недостаточно аргументов для выполнения последней команды!"));
        }
    }


    /**
     * Подготавливает запрос на основе введённой пользователем команды.
     * Если команда требует дополнительного ввода, запрашивает его у пользователя.
     *
     * @param console       Объект консоли для взаимодействия с пользователем.
     * @param inputCommand  Введённая пользователем команда.
     * @return Объект Request, содержащий команду и её аргументы.
     */
    private static Request prepareRequest(Console console, String inputCommand) {
        String[] commands = (inputCommand.trim() + " ").split(" ", 2);
        console.println("Введена команда '" + commands[0] + "' с аргументами: " + commands[1].trim());
        if (commandsData.get(commands[0]).getSecond()) {
            return askingRequest(console, inputCommand); // Если команда требует построчного ввода
        } else if (commands[0].equals("execute_script")) {
            ExecutionResponse scriptStatus = runScript(commands[1].trim());
            if (!scriptStatus.getExitCode()) {
                console.printError(scriptStatus.getAnswer());
                return null;
            }
            return null;
        } else {
            return new Request(inputCommand, user);
        }
    }







    /**
     * Выполняет файл скрипта и обрабатывает его команды.
     *
     * @param fileName Имя файла скрипта для выполнения.
     * @return ExecutionResponse, указывающий на успех или неудачу выполнения скрипта.
     */
    private static ExecutionResponse runScript(String fileName) {
        try {
            scriptStackCounter++;
            if (scriptStackCounter > 100) {
                scriptStackCounter--;
                return new ExecutionResponse(false, new AnswerString("Превышена максимальная глубина рекурсии!"));
            }
            if (fileName.isEmpty()) {
                scriptStackCounter--;
                return new ExecutionResponse(false, new AnswerString( "У команды execute_script должен быть ровно один аргумент!\nПример корректного ввода: execute_script file_name"));
            }
            console.println("Запуск скрипта '" + fileName + "'");
            try  {
                File file = new File(fileName);
                Console FileConsole = new FileConsole(file);
                Scanner scanner = new Scanner(file);
                while (scriptStackCounter > 0) {
                    String line = scanner.nextLine();
                    if (!scanner.hasNextLine()) break;
                    if (!line.equals("exit")) {

                        Request request = prepareRequest(FileConsole, line);
                        if (request == null) {
                            return new ExecutionResponse(false, new AnswerString( "Выполнение скрипта остановлено"));
                        }
                        networkManager.send(request);
                        Response response = networkManager.receive();
                        ExecutionResponse commandStatus = response.getExecutionStatus();

                        if (response.getExecutionStatus().getExitCode()) {
                            console.println(commandStatus.getAnswer().getAnswer());
                        } else {
                            if (!commandStatus.getAnswer().equals("Выполнение скрипта приостановлено.")) {
                                console.printError(commandStatus.getAnswer().getAnswer());
                            }
                            return new ExecutionResponse(false, new AnswerString("Выполнение скрипта приостановлено."));
                        }
                    } else {
                        scriptStackCounter--;
                        return new ExecutionResponse(true, new AnswerString("Скрипт успешно выполнен."));
                    }
                }
            } catch (FileNotFoundException e) {
                return new ExecutionResponse(false, new AnswerString("Не удаётся найти файл скрипта!"));
            } catch (IllegalArgumentException e) {
                return new ExecutionResponse(false,new AnswerString("Произошла ошибка при чтении данных из файла скрипта!"));
            } catch (Exception e) {
                return new ExecutionResponse(false,new AnswerString( ""));
            }
            return new ExecutionResponse(true, new AnswerString(""));
        } catch (Exception e) {
            return new ExecutionResponse(false, new AnswerString( "Произошла ошибка при запуске скрипта!"));
        }
    }
}