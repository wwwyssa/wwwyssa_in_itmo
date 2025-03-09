package commands;

import managers.CollectionManager;
import utils.DefaultConsole;
import utils.ExecutionResponse;

/**
 * Команда 'clear'. Очищает коллекцию.
 *
 * @author dim0n4eg
 */
public class Clear extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public Clear(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse execute(String[] arguments) {
        //if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        ExecutionResponse validationResponse = validate(arguments);
        if (!validationResponse.getExitCode()) return validationResponse;
        int i = 0;
        while (collectionManager.getCollection().size() > 0) {
            if (collectionManager.getCollection().containsKey(i)){
                collectionManager.removeProduct(i);
            }
            i++;
        }
        return new ExecutionResponse("Коллекция очищена!");
    }
}