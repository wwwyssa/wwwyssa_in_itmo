package commands;

import managers.CollectionManager;
import utils.Console;
import models.Product;
import utils.ExecutionResponse;

/**
 * Команда 'clear'. Очищает коллекцию.
 *
 * @author dim0n4eg
 */
public class Clear extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию", 0);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse execute(String[] arguments) {
        if (!arguments[1].isEmpty())
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        int i = 0;
        while (collectionManager.getCollection().size() > 0) {
            if (collectionManager.getCollection().containsKey(i)){
                collectionManager.removeProduct(i);
            }
            i++;
        }
        return new ExecutionResponse("Коллекция очищена!");
    }
}