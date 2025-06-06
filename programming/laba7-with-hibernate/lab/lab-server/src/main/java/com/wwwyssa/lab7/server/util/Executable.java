package com.wwwyssa.lab7.server.util;

import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.common.validators.ArgumentValidator;

public interface Executable {
    /**
     * Выполнить что-либо.
     *
     * @param arguments Аргумент для выполнения
     * @return результат выполнения
     */
    ExecutionResponse execute(String arguments, User user);

    ArgumentValidator getArgumentValidator();

    String getName();
}