package utils;

import utils.responses.ExecutionResponse;

public interface Executable {
    /**
     * Выполнить что-либо.
     *
     * @param arguments Аргумент для выполнения
     * @return результат выполнения
     */
    ExecutionResponse execute(String[] arguments);
}