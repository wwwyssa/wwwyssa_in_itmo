package commands;

import managers.CollectionManager;
import models.ProductReader;
import models.Product;
import utils.DefaultConsole;
import utils.ExecutionResponse;

public class RemoveGreater extends Command{
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;

    public RemoveGreater(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("remove_greater", "удалить из коллекции все элементы, превышающие заданный", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(String[] args) {
        //if (args.length != 1) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName());
        ExecutionResponse response = validate(args);
        if (!response.getExitCode()) return response;
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
