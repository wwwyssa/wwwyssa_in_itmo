package com.wwwyssa.lab6.server.commands;


import com.wwwyssa.lab6.common.util.User;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab6.server.managers.CollectionManager;

/**
 * Команда 'save'. Сохраняет коллекцию в файл.
 */
public class Save extends Command<NoArgumentsValidator> {
    private final CollectionManager collectionManager;

    public Save(CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл", 0, new NoArgumentsValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse innerExecute(String arguments, User user) {
        collectionManager.saveCollection();
        return new ExecutionResponse<>(true, new AnswerString("Коллекция успешно сохранена в файл."));
    }
}