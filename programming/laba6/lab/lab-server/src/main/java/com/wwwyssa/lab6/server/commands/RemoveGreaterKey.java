package com.wwwyssa.lab6.server.commands;


import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.IdValidator;
import com.wwwyssa.lab6.server.managers.CollectionManager;

public class RemoveGreaterKey extends Command<IdValidator> {
    private final CollectionManager collectionManager;

    public RemoveGreaterKey(CollectionManager collectionManager) {
        super("remove_greater_key", "удалить из коллекции все элементы, ключ которых превышает заданный", 1, new IdValidator());
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse innerExecute(String arguments) {
        try {
            String[] args = arguments.split(" ", 2);
            System.out.println(args[0]);
            int key = Integer.parseInt(arguments);
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
