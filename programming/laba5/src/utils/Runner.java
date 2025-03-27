package utils;

import java.util.NavigableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import commands.Command;
import managers.CommandManager;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;

/**
 * Класс для запуска команд в интерактивном режиме и режиме скрипта.
 */
public class Runner {
    private final DefaultConsole defaultConsole;
    private final CommandManager commandManager;
    private final List<String> scriptStack = new ArrayList<>();

    /**
     * Конструктор класса Runner.
     * @param defaultConsole объект Console для ввода/вывода
     * @param commandManager менеджер команд
     */
    public Runner(DefaultConsole defaultConsole, CommandManager commandManager) {
        this.defaultConsole = defaultConsole;
        this.commandManager = commandManager;
    }

    /**
     * Запускает команду.
     * @param userCommand команда для запуска
     * @return код завершения
     */
    private ExecutionResponse launchCommand(String[] userCommand) {
        if (userCommand[0].isEmpty()) return new ExecutionResponse(new AnswerString(""));
        Command command = commandManager.getCommands().get(userCommand[0]);

        if (command == null)
            return new ExecutionResponse(false, new AnswerString("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки"));
        if (command.getName().equals("execute_script")) {
            return command.execute(userCommand);
        }
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

                userCommand = (defaultConsole.input().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                commandManager.addToHistory(userCommand[0]);
                commandStatus = launchCommand(userCommand);

                if (commandStatus.getAnswer().getAnswer().equals("exit")) break;
                defaultConsole.println(commandStatus.getAnswer().getAnswer());
            }
        } catch (NoSuchElementException exception) {
            defaultConsole.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            defaultConsole.printError("Непредвиденная ошибка!");
        }
    }
}