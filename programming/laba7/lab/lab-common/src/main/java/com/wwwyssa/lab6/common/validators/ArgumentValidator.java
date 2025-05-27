package com.wwwyssa.lab6.common.validators;

import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;

/**
 * Абстрактный класс для валидаторов аргументов команд.
 */

public abstract class ArgumentValidator {
    /**
     * Проверяет аргумент команды.
     *
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    public abstract ExecutionResponse validate(String arg, String name);
}