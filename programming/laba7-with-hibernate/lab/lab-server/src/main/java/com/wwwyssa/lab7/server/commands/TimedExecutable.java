package com.wwwyssa.lab7.server.commands;
import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.validators.ArgumentValidator;
import com.wwwyssa.lab7.server.util.Executable;

import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;

public class TimedExecutable implements Executable {
    private final Executable wrappedCommand;

    public TimedExecutable(Executable wrappedCommand) {
        this.wrappedCommand = wrappedCommand;
    }

    @Override
    public ExecutionResponse execute(String arguments, User user) {
        long startTime = System.nanoTime();

        ExecutionResponse result = wrappedCommand.execute(arguments, user);

        long endTime = System.nanoTime();
        long durationMillis = (endTime - startTime) / 1_000_000;

        System.out.println("Время выполнения команды: " + durationMillis + " мс");

        return result;
    }

    @Override
    public ArgumentValidator getArgumentValidator() {
        return wrappedCommand.getArgumentValidator();
    }

    @Override
    public String getName() {
        return wrappedCommand.getName();
    }
}
