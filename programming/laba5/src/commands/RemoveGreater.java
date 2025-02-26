package commands;

import managers.CollectionManager;
import models.ProductReader;
import models.Product;
import utils.Console;
import utils.ExecutionResponse;
import utils.*;

public class RemoveGreater extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveGreater(Console console, CollectionManager collectionManager) {
        super("remove_greater", "удалить из коллекции все элементы, превышающие заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(String[] args) {
        if (args.length != 1) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName());
        try{
            Product product = ProductReader.readProduct(console, collectionManager.getFreeId());
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
