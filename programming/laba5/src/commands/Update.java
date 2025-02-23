package commands;

import managers.CollectionManager;
import utils.Console;
import models.ObjectReader;
import utils.ExecutionResponse;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 * @author dim0n4eg
 */
public class Update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Update(Console console, CollectionManager collectionManager) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    public ExecutionResponse execute(String[] arguments) {
        try {
            if (arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
            long id = -1;
            try { id = Long.parseLong(arguments[1].trim()); } catch (NumberFormatException e) { return new ExecutionResponse(false, "Ошибка ввода ID "); }

            var old = collectionManager.getById(id);
            if (old == null) {
                return new ExecutionResponse(false, "Не существующий ID");
            }

            console.println("* Создание нового Продукта:");
            var d = ObjectReader.readProduct(console, old.getId());
            if (d != null && d.isValid()) {
                collectionManager.removeProduct(old.getId());
                collectionManager.addProduct(d);
                return new ExecutionResponse("Обновлено!");
            } else {
                return new ExecutionResponse(false, "Поля продукта не валидны! Продукт не создан!");
            }
        } catch (ObjectReader.ObjectReaderBreak e) {
            return new ExecutionResponse(false, "Поля Продукта не валидны! Продукт не создан!");
        }
    }
}