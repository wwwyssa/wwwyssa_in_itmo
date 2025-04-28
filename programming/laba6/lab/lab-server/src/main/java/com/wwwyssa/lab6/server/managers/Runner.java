package com.wwwyssa.lab6.server.managers;

import java.util.NavigableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.server.commands.Command;
import com.wwwyssa.lab6.server.managers.CommandManager;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;

/**
 * Класс для запуска команд в интерактивном режиме и режиме скрипта.
 */
public class Runner {
    private final CommandManager commandManager;
    private final List<String> scriptStack = new ArrayList<>();

    /**
     * Конструктор класса Runner.
     * @param commandManager менеджер команд
     */
    public Runner(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Запускает команду.
     * @param userCommand команда для запуска
     * @return код завершения
     */
    public ExecutionResponse launchCommand(String[] userCommand, Product product) {
        if (userCommand[0].isEmpty()) return new ExecutionResponse(new AnswerString(""));
        Command command = commandManager.getCommands().get(userCommand[0]);
        if (command == null)
            return new ExecutionResponse(false, new AnswerString("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки"));
        if (command.getName().equals("execute_script")) {
            return command.execute(userCommand[1]);
        }
        return command.execute(userCommand[1]);
    }
}