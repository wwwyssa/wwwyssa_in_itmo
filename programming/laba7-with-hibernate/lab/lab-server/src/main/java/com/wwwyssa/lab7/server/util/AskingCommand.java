package com.wwwyssa.lab7.server.util;


import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.util.executions.AnswerString;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.common.validators.ArgumentValidator;
import com.wwwyssa.lab7.common.validators.IdValidator;
import com.wwwyssa.lab7.common.models.Product;
import com.wwwyssa.lab7.server.commands.Command;

/**
 * Абстрактный класс для команд, требующих ввода данных.
 * @param <T> Тип валидатора аргументов.
 */
public abstract class AskingCommand<T extends ArgumentValidator> extends Command<T> {

    /**
     * Конструктор команды AskingCommand.
     *
     * @param name Имя команды.
     * @param description Описание команды.
     * @param argumentValidator Валидатор аргументов команды.
     */
    public AskingCommand(String name, String description, T argumentValidator) {
        super(name, description, 0, argumentValidator);
    }

    /**
     * Выполняет команду с аргументом.
     *
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionResponse innerExecute(String arg,  User user) {
        return null;
    }

    /**
     * Выполняет команду с элементом коллекции.
     *
     * @return Статус выполнения команды.
     */
    protected abstract ExecutionResponse innerExecute(Product product, User user);

    /**
     * Запускает выполнение команды.
     *
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    public ExecutionResponse execute(String arg, Product product, User user) {
        ExecutionResponse argumentStatus = argumentValidator.validate(arg, getName());
        if (argumentStatus.getExitCode()) {
            return innerExecute(product, user);
        } else {
            return argumentStatus;
        }
    }

    @Override
    public ExecutionResponse execute(String arg, User user) {
        return new ExecutionResponse(false, new AnswerString("Метод должен вызываться с аргументом Product!"));
    }
}