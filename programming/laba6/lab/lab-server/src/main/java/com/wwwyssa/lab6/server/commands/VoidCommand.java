package com.wwwyssa.lab6.server.commands;

import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab6.server.managers.CommandManager;

import java.util.Map;

public class VoidCommand extends Command<NoArgumentsValidator>  {
    private final CommandManager commandManager;


    public VoidCommand(String name, String description, CommandManager commandManager) {
        super(name, description, 0, new NoArgumentsValidator());
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    public ExecutionResponse innerExecute(String arguments) {

        return new ExecutionResponse<>(new AnswerString(""));
    }
}
