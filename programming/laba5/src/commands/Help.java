package commands;

import managers.CommandManager;
import utils.Console;
import utils.ExecutionResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Команда 'help'. Выводит справку по доступным командам
 **/
public class Help extends Command {
    private final Console console;
    private final CommandManager commandManager;

    public Help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    public ExecutionResponse execute(String[] arguments) {
        if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");


        String result = "";
        for (Map.Entry<String, Command> entry : commandManager.getCommands().entrySet()) {
            result += entry.getKey() + " -> " + entry.getValue() + "\n";
        }
        return new ExecutionResponse(result);
    }
}