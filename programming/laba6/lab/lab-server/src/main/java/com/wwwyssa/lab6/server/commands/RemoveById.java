package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;
import utils.responses.ValidAnswer;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 */
public class RemoveById extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public RemoveById(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("remove_by_id <ID>", "удалить элемент из коллекции по ID", 1);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse<ValidAnswer<String>> innerExecute(String[] arguments) {
        long id = -1;
        try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { return new ExecutionResponse<>(false, new AnswerString("ID не распознан")); }

        if (collectionManager.getById(id) == null )
            return new ExecutionResponse<>(false, new AnswerString("Не существующий ID"));
        collectionManager.removeProduct(id);
        return new ExecutionResponse<>(new AnswerString("Продукт успешно удалён!"));
    }
}