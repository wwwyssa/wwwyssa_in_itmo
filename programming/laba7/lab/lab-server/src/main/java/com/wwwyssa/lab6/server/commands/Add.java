package com.wwwyssa.lab6.server.commands;


import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.common.util.User;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab6.server.managers.CollectionManager;
import com.wwwyssa.lab6.server.util.AskingCommand;

/**
 * Класс, представляющий команду добавления нового продукта в коллекцию.
 */
public class Add  extends AskingCommand<NoArgumentsValidator> {
    private final CollectionManager collectionManager;

    /**
     * Конструктор для создания объекта Add.
     * @param collectionManager менеджер коллекции для управления продуктами
     */
    public Add(CollectionManager collectionManager) {
        super("add {Product}", "add new Product to collection", new NoArgumentsValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления нового продукта в коллекцию.
     * @return результат выполнения команды
     */
    @Override
    public ExecutionResponse innerExecute(Product product, User user) {
        System.out.println(product);
        CollectionManager.getInstance().addProduct(product, user);
        return new ExecutionResponse(true, new AnswerString("Элемент успешно добавлен в коллекцию!"));
    }
}