package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.responses.Answer;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;

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

    @Override
    public ExecutionResponse innerExecute(String[] arguments) {
        return new ExecutionResponse(new AnswerString(collectionManager.toString()));
    }
}