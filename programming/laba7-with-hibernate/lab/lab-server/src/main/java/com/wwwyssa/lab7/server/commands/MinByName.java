package com.wwwyssa.lab7.server.commands;


import com.wwwyssa.lab7.common.models.Product;
import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.util.executions.AnswerString;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab7.server.managers.CollectionManager;


public class MinByName extends Command<NoArgumentsValidator> {
    private final CollectionManager collectionManager;

    public MinByName(CollectionManager collectionManager) {
        super("min_by_name", "вывести объект из коллекции, имя которого является минимальным", 0, new NoArgumentsValidator());
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse innerExecute(String arguments, User user) {
        if (collectionManager.getCollection().isEmpty()) { return new ExecutionResponse<>(false, new AnswerString("Коллекция пуста."));}
        Product minProduct = null;
        String minName = null;
        for (Product product : collectionManager.getCollection().values()) {
            if (minName == null || product.getName().compareTo(minName) < 0) {
                minName = product.getName();
                minProduct = product;
            }
        }
        if (minProduct == null) {
            return new ExecutionResponse<>(false, new AnswerString("Произошла ошибка при поиске элемента с минимальным именем."));
        }
        return new ExecutionResponse<>(true, new AnswerString(minProduct.toString()));
    }
}
