package commands;

import managers.CollectionManager;
import models.Product;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;

public class MinByName extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public MinByName(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("min_by_name", "вывести объект из коллекции, имя которого является минимальным", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse innerExecute(String[] arguments) {
        if (collectionManager.getCollection().isEmpty()) { return new ExecutionResponse(false, new AnswerString("Коллекция пуста."));}
        Product minProduct = null;
        String minName = null;
        for (Product product : collectionManager.getCollection().values()) {
            if (minName == null || product.getName().compareTo(minName) < 0) {
                minName = product.getName();
                minProduct = product;
            }
        }
        if (minProduct == null) {
            return new ExecutionResponse(false, new AnswerString("Произошла ошибка при поиске элемента с минимальным именем."));
        }
        return new ExecutionResponse(true, new AnswerString(minProduct.toString()));
    }
}
