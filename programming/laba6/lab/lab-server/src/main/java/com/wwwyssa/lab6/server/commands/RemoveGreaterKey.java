package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;
import utils.responses.ValidAnswer;

public class RemoveGreaterKey extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public RemoveGreaterKey(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("remove_greater_key", "удалить из коллекции все элементы, ключ которых превышает заданный", 1);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse<ValidAnswer<String>> innerExecute(String[] arguments) {
        try {
            int key = Integer.parseInt(arguments[1]);
            for (int keyToRemove : collectionManager.getCollection().keySet()) {
                if (keyToRemove > key) {
                    collectionManager.removeProduct(keyToRemove);
                }
            }
            return new ExecutionResponse<>(true, new AnswerString("Все элементы с ключом больше " + key + " успешно удалены."));
        } catch (NumberFormatException e) {
            return new ExecutionResponse<>(false,new AnswerString( "Ключ должен быть числом."));
        }
    }
}
