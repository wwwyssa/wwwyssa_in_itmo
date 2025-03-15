package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.responses.ExecutionResponse;

public class RemoveGreaterKey extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public RemoveGreaterKey(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("remove_greater_key", "удалить из коллекции все элементы, ключ которых превышает заданный", 1);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse innerExecute(String[] arguments) {
        try {
            int key = Integer.parseInt(arguments[1]);
            for (int keyToRemove : collectionManager.getCollection().keySet()) {
                if (keyToRemove > key) {
                    collectionManager.removeProduct(keyToRemove);
                }
            }
            return new ExecutionResponse(true, "Все элементы с ключом больше " + key + " успешно удалены.");
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "Ключ должен быть числом.");
        }
    }
}
