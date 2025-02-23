package commands;

import managers.CollectionManager;
import models.ObjectReader;
import models.Product;
import utils.Console;
import utils.ExecutionResponse;

/**
 * Класс, представляющий команду добавления нового продукта в коллекцию.
 */
public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор для создания объекта Add.
     * @param console объект консоли для взаимодействия с пользователем
     * @param collectionManager менеджер коллекции для управления продуктами
     */
    public Add(Console console, CollectionManager collectionManager) {
        super("add {Product}", "add new Product to collection");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления нового продукта в коллекцию.
     * @param args аргументы команды
     * @return результат выполнения команды
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        try {
            if (!args[1].isEmpty()) return new ExecutionResponse(false, "Incorrect number of arguments!\nCorrect: '" + getName() + "'");

            console.println("* Making new Product:");
            Product product = ObjectReader.readProduct(console, collectionManager.getFreeId());

            //todo isValid check
            if (product != null) {
                console.println(product.isValid());
                collectionManager.addProduct(product);
                return new ExecutionResponse("Product successfully added!");
            } else return new ExecutionResponse(false,"Product fields are not valid! Product is not created!");
        } catch (ObjectReader.ObjectReaderBreak e) {
            return new ExecutionResponse(false,"Cancel...");
        }
    }
}