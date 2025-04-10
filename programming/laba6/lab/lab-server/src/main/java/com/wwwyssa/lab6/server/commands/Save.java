package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;
import utils.responses.ValidAnswer;

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
    public ExecutionResponse<ValidAnswer<String>> innerExecute(String[] arguments) {
        collectionManager.saveCollection();
        return new ExecutionResponse<>(true, new AnswerString("Коллекция успешно сохранена в файл."));
    }
}