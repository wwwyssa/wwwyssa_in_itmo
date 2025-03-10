package commands;

import managers.CollectionManager;
import models.Product;
import utils.console.DefaultConsole;
import utils.ExecutionResponse;

public class MinByName extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public MinByName(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("min_by_name", "вывести объект из коллекции, имя которого является минимальным", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(String[] arguments) {
        if (collectionManager.getCollection().isEmpty()) { return new ExecutionResponse(false, "Коллекция пуста.");}
        ExecutionResponse response = validate(arguments);
        if (!response.getExitCode()) return response;

        Product minProduct = null;
        String minName = null;
        for (Product product : collectionManager.getCollection().values()) {
            if (minName == null || product.getName().compareTo(minName) < 0) {
                minName = product.getName();
                minProduct = product;
            }
        }
        if (minProduct == null) {
            return new ExecutionResponse(false, "Произошла ошибка при поиске элемента с минимальным именем.");
        }
        return new ExecutionResponse(true, minProduct.toString());
    }
}
