package com.wwwyssa.lab6.server.commands;

import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.IdValidator;
import com.wwwyssa.lab6.server.managers.CollectionManager;


/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 */
public class RemoveById extends Command<IdValidator> {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id <ID>", "удалить элемент из коллекции по ID", 1, new IdValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse innerExecute(String args) {
        long id = -1;
        try { id = Long.parseLong(args); } catch (NumberFormatException e) { return new ExecutionResponse<>(false, new AnswerString("ID не распознан")); }
        if (collectionManager.getById(id) == null )
            return new ExecutionResponse<>(false, new AnswerString("Не существующий ID"));
        collectionManager.removeProduct(id);
        return new ExecutionResponse<>(new AnswerString("Продукт успешно удалён!"));
    }
}