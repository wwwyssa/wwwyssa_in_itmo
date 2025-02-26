package utils;

import commands.Command;
import managers.CollectionManager;
import models.Product;
import models.ProductReader;

public class ReplaceIfLower extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public ReplaceIfLower(Console console, CollectionManager collectionManager) {
        super("replace_if_lower", "заменить значение по ключу, если новое значение меньше старого");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 2)
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName());
        int key = Integer.parseInt(args[1]);
        if (!collectionManager.getCollection().containsKey(key)) {
            console.println("Элемента с таким ключом нет в коллекции");
            return new ExecutionResponse(false, "Элемента с таким ключом нет в коллекции");
        }
        try {
            Product product = ProductReader.readProduct(console, collectionManager.getFreeId());
            if (collectionManager.getCollection().get(key).compareTo(product) > 0) {
                collectionManager.removeProduct(key);
                collectionManager.addProductWithKey(key, product);
                console.println("Продукт успешно заменен!");
                return new ExecutionResponse("Продукт успешно заменен!");
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка ввода данных!");
        }
        return new ExecutionResponse(false, "Новое значение не меньше старого!");
    }
}
