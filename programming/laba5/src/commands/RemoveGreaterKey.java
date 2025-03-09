package commands;

import managers.CollectionManager;
import utils.Console;
import utils.ExecutionResponse;

public class RemoveGreaterKey extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveGreaterKey(Console console, CollectionManager collectionManager) {
        super("remove_greater_key", "удалить из коллекции все элементы, ключ которых превышает заданный", 1);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(String[] arguments) {
        if (arguments.length != 2) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "{id}");

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
