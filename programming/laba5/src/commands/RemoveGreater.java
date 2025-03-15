package commands;

import managers.CollectionManager;
import models.ProductReader;
import models.Product;
import utils.console.DefaultConsole;
import utils.responses.ExecutionResponse;

public class RemoveGreater extends Command{
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public RemoveGreater(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("remove_greater", "удалить из коллекции все элементы, превышающие заданный", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse innerExecute(String[] args) {
        try{
            Product product = ProductReader.readProduct(defaultConsole, collectionManager.getFreeId());
            int count = 0;
            for(Integer key : collectionManager.getCollection().keySet()){
                if (collectionManager.getCollection().get(key).compareTo(product) > 0){
                    collectionManager.removeProduct(key);
                    count++;
                }
            }
            return new ExecutionResponse("Продукты успешно удалены! Удалено " + count + " элементов");
        } catch (Exception e){
            return new ExecutionResponse(false, "Ошибка ввода данных!");
        }


    }

}
