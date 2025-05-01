package com.wwwyssa.lab6.server.commands;


import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab6.server.managers.CollectionManager;
import com.wwwyssa.lab6.server.util.AskingCommand;

public class RemoveGreater extends AskingCommand<NoArgumentsValidator> {
    private final CollectionManager collectionManager;

    public RemoveGreater(CollectionManager collectionManager) {
        super("remove_greater", "удалить из коллекции все элементы, превышающие заданный", new NoArgumentsValidator());
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse innerExecute(Product product) {
        try{
            int count = 0;
            CollectionManager collectionManager1 = CollectionManager.getInstance();
            for(Integer key : collectionManager1.getCollection().keySet()){
                if (collectionManager1.getCollection().get(key).compareTo(product) > 0){
                    CollectionManager.getInstance().removeProduct(key);
                    count++;
                }
            }
            return new ExecutionResponse<>(new AnswerString("Продукты успешно удалены! Удалено " + count + " элементов"));
        } catch (Exception e){
            System.out.println(e);
            return new ExecutionResponse<>(false, new AnswerString("Ошибка ввода данных!"));
        }


    }

}
