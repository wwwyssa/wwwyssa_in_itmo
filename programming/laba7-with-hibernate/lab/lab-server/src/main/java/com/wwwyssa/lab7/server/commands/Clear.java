package com.wwwyssa.lab7.server.commands;

import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.util.executions.AnswerString;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab7.server.managers.CollectionManager;


/**
 * Команда 'clear'. Очищает коллекцию.
 *
 * @author dim0n4eg
 */
public class Clear extends Command<NoArgumentsValidator> {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию", 0, new NoArgumentsValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse innerExecute(String arguments, User user) {
        int i = 0;
        while (collectionManager.getCollection().size() > 0) {
            if (collectionManager.getCollection().containsKey(i)){
                collectionManager.removeProduct(i, user);
            }
            i++;
        }
        return new ExecutionResponse(new AnswerString("Коллекция очищена!"));
    }
}