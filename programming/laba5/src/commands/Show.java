package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.ExecutionResponse;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 */
public class Show extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public Show(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */

    public ExecutionResponse execute(String[] arguments) {
        //if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        ExecutionResponse response = validate(arguments);
        if (!response.getExitCode()) return response;
        return new ExecutionResponse(collectionManager.toString());

    }
}