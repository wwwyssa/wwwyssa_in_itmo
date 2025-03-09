package commands;

import managers.CollectionManager;
import utils.DefaultConsole;
import utils.ExecutionResponse;

/**
 * Команда 'save'. Сохраняет коллекцию в файл.
 */
public class Save extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public Save(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse execute(String[] arguments) {
        //if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        ExecutionResponse response = validate(arguments);
        if (!response.getExitCode()) return response;
        collectionManager.saveCollection();
        return new ExecutionResponse(true, "Коллекция успешно сохранена в файл.");
    }
}