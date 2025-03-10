package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import models.ProductReader;
import utils.ExecutionResponse;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 * @author dim0n4eg
 */
public class Update extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public Update(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID", 1);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    public ExecutionResponse execute(String[] arguments) {
        try {
            //if (arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
            ExecutionResponse response = validate(arguments);
            if (!response.getExitCode()) return response;
            long id = -1;
            try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { return new ExecutionResponse(false, "Ошибка ввода ID "); }

            var old = collectionManager.getById(id);
            if (old == null) {
                return new ExecutionResponse(false, "Не существующий ID");
            }

            defaultConsole.println("* Создание нового Продукта:");
            var d = ProductReader.readProduct(defaultConsole, old.getId());
            if (d != null && d.isValid()) {
                collectionManager.removeProduct(old.getId());
                collectionManager.addProduct(d);
                return new ExecutionResponse("Обновлено!");
            } else {
                return new ExecutionResponse(false, "Поля продукта не валидны! Продукт не создан!");
            }
        } catch (ProductReader.ObjectReaderBreak e) {
            return new ExecutionResponse(false, "Поля Продукта не валидны! Продукт не создан!");
        }
    }
}