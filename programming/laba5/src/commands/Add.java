package commands;

import managers.CollectionManager;
import models.ProductReader;
import models.Product;
import utils.console.Console;
import utils.responses.Answer;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;

/**
 * Класс, представляющий команду добавления нового продукта в коллекцию.
 */
public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор для создания объекта Add.
     * @param defaultConsole объект консоли для взаимодействия с пользователем
     * @param collectionManager менеджер коллекции для управления продуктами
     */
    public Add(Console defaultConsole, CollectionManager collectionManager) {
        super("add {Product}", "add new Product to collection", 0);
        this.console = defaultConsole;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления нового продукта в коллекцию.
     * @param args аргументы команды
     * @return результат выполнения команды
     */
    @Override
    public ExecutionResponse innerExecute(String[] args) {
        try {
            Product product = ProductReader.readProduct(console, collectionManager.getFreeId());
            if (product != null) {
                collectionManager.addProduct(product);
                return new ExecutionResponse(new AnswerString("Product successfully added!"));
            } else return new ExecutionResponse(false,new AnswerString("Product fields are not valid! Product is not created!"));
        } catch (ProductReader.ObjectReaderBreak e) {
            return new ExecutionResponse(false,new AnswerString("Cancel..."));
        }
    }
}