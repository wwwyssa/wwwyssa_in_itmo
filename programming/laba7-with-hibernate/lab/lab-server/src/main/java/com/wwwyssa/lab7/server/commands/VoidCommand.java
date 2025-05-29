package com.wwwyssa.lab7.server.commands;

import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.util.executions.AnswerString;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab7.server.managers.CommandManager;

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
    public ExecutionResponse innerExecute(String arguments, User user) {

        return new ExecutionResponse<>(new AnswerString(""));
    }
}
