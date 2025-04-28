package com.wwwyssa.lab6.server.util;

import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;

public interface Executable {
    /**
     * Выполнить что-либо.
     *
     * @param arguments Аргумент для выполнения
     * @return результат выполнения
     */
    ExecutionResponse execute(String[] arguments);
}