package com.wwwyssa.lab6.common.util;



import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.ArgumentValidator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    private Map<String, Pair<ArgumentValidator, Boolean>> commandsData; // Второе значение - true, если команда требует ввода элемента коллекции
    private ExecutionResponse executionStatus;

    public Response(Map<String, Pair<ArgumentValidator, Boolean>> commandsData) {
        this.commandsData = commandsData;
    }

    public Response(ExecutionResponse executionStatus) {
        this.executionStatus = executionStatus;
    }

    public ExecutionResponse getExecutionStatus() {
        return executionStatus;
    }

    public Map<String, Pair<ArgumentValidator, Boolean>> getCommandsMap() {
        return commandsData;
    }

    @Override
    public String toString() {
        return "Response{" +
                "commandsData=" + commandsData +
                ", executionStatus='" + executionStatus + '\'' +
                '}';
    }
}