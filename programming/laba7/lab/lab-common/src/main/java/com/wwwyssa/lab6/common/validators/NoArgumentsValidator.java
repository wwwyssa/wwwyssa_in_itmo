package com.wwwyssa.lab6.common.validators;

import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;

import java.io.Serial;
import java.io.Serializable;

/**
 * Валидатор для проверки отсутствия аргументов у команды.
 */
public class NoArgumentsValidator extends ArgumentValidator implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    /**
     * Проверяет корректность аргумента команды.
     *
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    @Override
    public ExecutionResponse validate(String arg, String name) {
        if (!arg.isEmpty()) {
            return new ExecutionResponse(false, new AnswerString("У команды нет аргументов!\nПример корректного ввода: " + name));
        }
        return new ExecutionResponse(true, new AnswerString("Аргумент команды введен корректно."));
    }
}