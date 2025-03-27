package commands;

import managers.CommandManager;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;
import utils.responses.ValidAnswer;

import java.util.Map;

/**
 * Команда 'help'. Выводит справку по доступным командам
 **/
public class Help extends Command {
    private final DefaultConsole defaultConsole;
    private final CommandManager commandManager;

    public Help(DefaultConsole defaultConsole, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам", 0);
        this.defaultConsole = defaultConsole;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    public ExecutionResponse<ValidAnswer<String>> innerExecute(String[] arguments) {
        String result = "";
        for (Map.Entry<String, Command> entry : commandManager.getCommands().entrySet()) {
            result += entry.getKey() + " -> " + entry.getValue() + "\n";
        }
        return new ExecutionResponse<>(new AnswerString(result));
    }
}