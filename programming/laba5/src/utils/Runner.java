package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.nio.file.*;

import commands.Command;
import managers.CommandManager;

/**
 * Класс для запуска команд в интерактивном режиме и режиме скрипта.
 */
public class Runner {
    private final Console console;
    private final CommandManager commandManager;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;

    /**
     * Конструктор класса Runner.
     * @param console объект Console для ввода/вывода
     * @param commandManager менеджер команд
     */
    public Runner(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Запускает команду.
     * @param userCommand команда для запуска
     * @return код завершения
     */
    private ExecutionResponse launchCommand(String[] userCommand) {
        if (userCommand[0].isEmpty()) return new ExecutionResponse("");
        Command command = commandManager.getCommands().get(userCommand[0]);

        if (command == null) return new ExecutionResponse(false, "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
        return command.execute(userCommand);

    }

    /**
     * Интерактивный режим.
     */
    public void interactiveMode() {
        try {
            ExecutionResponse commandStatus;
            String[] userCommand = {"", ""};

            while (true) {
                userCommand = (console.input().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                commandManager.addToHistory(userCommand[0]);
                commandStatus = launchCommand(userCommand);

                if (commandStatus.getMessage().equals("exit")) break;
                console.println(commandStatus.getMessage());
            }
        } catch (NoSuchElementException exception) {
            console.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        }
    }

    /**
     * Проверяет рекурсивность выполнения скриптов.
     * @param argument название запускаемого скрипта
     * @param scriptScanner сканер для чтения скрипта
     * @return можно ли выполнять скрипт
     */
    private boolean checkRecursion(String argument, Scanner scriptScanner) {
        int recStart = -1;
        int i = 0;
        for (String script : scriptStack) {
            i++;
            if (argument.equals(script)) {
                if (recStart < 0) recStart = i;
                if (lengthRecursion < 0) {
                    console.selectConsoleScanner();
                    console.println("Была замечена рекурсия! Введите максимальную глубину рекурсии (0..100)");
                    while (lengthRecursion < 0 || lengthRecursion > 500) {
                        try { console.print("> "); lengthRecursion = Integer.parseInt(console.input().trim()); } catch (NumberFormatException e) { console.println("длина не распознана"); }
                    }
                    console.selectFileScanner(scriptScanner);
                }
                if (i > recStart + lengthRecursion || i > 100)
                    return false;
            }
        }
        return true;
    }

    /**
     * Режим для запуска скрипта.
     * @param argument аргумент скрипта
     * @return код завершения
     */
    /*
    private ExecutionResponse scriptMode(String argument) {
        String[] userCommand = {"", ""};
        StringBuilder executionOutput = new StringBuilder();

        if (!new File(argument).exists()) return new ExecutionResponse(false, "Файл не существет!");
        if (!Files.isReadable(Paths.get(argument))) return new ExecutionResponse(false, "Прав для чтения нет!");

        scriptStack.add(argument);
        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            ExecutionResponse commandStatus;

            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);
            do {
                userCommand = (console.input().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (console.isCanReadln() && userCommand[0].isEmpty()) {
                    userCommand = (console.input().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                boolean needLaunch = true;
                if (userCommand[0].equals("execute_script")) {
                    needLaunch = checkRecursion(userCommand[1], scriptScanner);
                }

                commandStatus = needLaunch ? launchCommand(userCommand) : new ExecutionResponse("Превышена максимальная глубина рекурсии");
                if (userCommand[0].equals("execute_script")) console.selectFileScanner(scriptScanner);
                executionOutput.append(commandStatus.getMessage() + "\n");
            } while (commandStatus.getExitCode() && !commandStatus.getMessage().equals("exit") && console.isCanReadln());

            console.selectConsoleScanner();
            if (!commandStatus.getExitCode() && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                executionOutput.append("Проверьте скрипт на корректность введенных данных!\n");
            }

            return new ExecutionResponse(commandStatus.getExitCode(), executionOutput.toString());
        } catch (FileNotFoundException exception) {
            return new ExecutionResponse(false, "Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            return new ExecutionResponse(false, "Файл со скриптом пуст!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return new ExecutionResponse("");
    }*/
}