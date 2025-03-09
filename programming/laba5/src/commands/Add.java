package commands;

import managers.CollectionManager;
import models.ProductReader;
import models.Product;
import utils.Console;
import utils.DefaultConsole;
import utils.ExecutionResponse;

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
    public ExecutionResponse execute(String[] args) {
        try {
            //if (!args[1].isEmpty()) return new ExecutionResponse(false, "Incorrect number of arguments!\nCorrect: '" + getName() + "'");
            ExecutionResponse validateResponse = validate(args);
            if (!validateResponse.getExitCode()) return validateResponse;
            //console.println(args.length);
            //console.println("* Making new Product:");
            Product product = ProductReader.readProduct(console, collectionManager.getFreeId());

            //todo isValid check
            if (product != null) {
                //console.println(product.isValid());
                collectionManager.addProduct(product);
                return new ExecutionResponse("Product successfully added!");
            } else return new ExecutionResponse(false,"Product fields are not valid! Product is not created!");
        } catch (ProductReader.ObjectReaderBreak e) {
            return new ExecutionResponse(false,"Cancel...");
        }
    }
}