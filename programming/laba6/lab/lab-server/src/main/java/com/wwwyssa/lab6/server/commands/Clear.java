package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;
import utils.responses.ValidAnswer;

/**
 * Команда 'clear'. Очищает коллекцию.
 *
 * @author dim0n4eg
 */
public class Clear extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public Clear(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse<AnswerString> innerExecute(String[] arguments) {
        int i = 0;
        while (collectionManager.getCollection().size() > 0) {
            if (collectionManager.getCollection().containsKey(i)){
                collectionManager.removeProduct(i);
            }
            i++;
        }
        return new ExecutionResponse(new AnswerString("Коллекция очищена!"));
    }
}