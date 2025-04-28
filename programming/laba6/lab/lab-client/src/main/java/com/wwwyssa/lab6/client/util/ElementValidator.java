package com.wwwyssa.lab6.client.util;

import com.wwwyssa.lab6.client.managers.Asker;
import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.util.Pair;

/**
 * Валидатор для проверки корректности элемента коллекции.
 */
public class ElementValidator {
    /**
     * Проверяет корректность введенного элемента коллекции.
     *
     * @param console Консоль для ввода/вывода.
     * @param id Идентификатор элемента коллекции.
     * @return Пара, содержащая статус выполнения проверки и элемент коллекции.
     */
    public Pair<ExecutionResponse, Product> validateAsking(Console console, long id) {
        try {
            Product product = Asker.readProduct(console, id);
            return validating(product);
        }  catch (Asker.IllegalInputException e) {
            return new Pair<>(new ExecutionResponse(false,new AnswerString(e.getMessage())), null);
        } catch (Asker.ObjectReaderBreak e) {
            throw new RuntimeException(e);
        }
    }

    public Pair<ExecutionResponse, Product> validating(Product product) {
        if (product != null && product.isValid()) {
            return new Pair<>(new ExecutionResponse(true, new AnswerString("Элемент введён корректно!")), product);
        }
        return new Pair<>(new ExecutionResponse(false,new AnswerString("Введены некорректные данные!")), null);
    }
}