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
    public void run() {
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
}