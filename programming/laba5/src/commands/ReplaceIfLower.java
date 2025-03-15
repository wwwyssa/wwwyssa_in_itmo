package commands;

import managers.CollectionManager;
import models.Product;
import models.ProductReader;
import utils.responses.ExecutionResponse;
import utils.console.DefaultConsole;

public class ReplaceIfLower extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public ReplaceIfLower(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("replace_if_lower", "заменить значение по ключу, если новое значение меньше старого", 1);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse innerExecute(String[] args) {
        int key = Integer.parseInt(args[1]);
        if (!collectionManager.getCollection().containsKey(key)) {
            return new ExecutionResponse(false, "Элемента с таким ключом нет в коллекции");
        }
        try {
            Product product = ProductReader.readProduct(defaultConsole, collectionManager.getFreeId());
            if (collectionManager.getCollection().get(key).compareTo(product) > 0) {
                collectionManager.removeProduct(key);
                collectionManager.addProductWithKey(key, product);
                defaultConsole.println("Продукт успешно заменен!");
                return new ExecutionResponse("Продукт успешно заменен!");
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка ввода данных!");
        }
        return new ExecutionResponse(false, "Новое значение не меньше старого!");
    }
}
