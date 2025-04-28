package com.wwwyssa.lab6.server.commands;

import com.wwwyssa.lab6.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.util.ValidAnswer;
import com.wwwyssa.lab6.server.managers.CommandManager;

import java.util.Map;

/**
 * Команда 'help'. Выводит справку по доступным командам
 **/
public class Help extends Command<NoArgumentsValidator>  {
    private final CommandManager commandManager;


    public Help(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам", 0, new NoArgumentsValidator());
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    public ExecutionResponse innerExecute(String arguments) {
        String result = "";
        for (Map.Entry<String, Command> entry : commandManager.getCommands().entrySet()) {
            result += entry.getKey() + " -> " + entry.getValue() + "\n";
        }
        return new ExecutionResponse<>(new AnswerString(result));
    }
}