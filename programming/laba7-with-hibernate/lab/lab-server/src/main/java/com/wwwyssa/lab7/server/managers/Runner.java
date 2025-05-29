package com.wwwyssa.lab7.server.managers;

import java.util.ArrayList;
import java.util.List;

import com.wwwyssa.lab7.common.models.Product;
import com.wwwyssa.lab7.common.util.User;
import com.wwwyssa.lab7.common.validators.ArgumentValidator;
import com.wwwyssa.lab7.server.Server;
import com.wwwyssa.lab7.common.util.executions.AnswerString;
import com.wwwyssa.lab7.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab7.server.util.AskingCommand;
import com.wwwyssa.lab7.server.util.Executable;

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


    private ExecutionResponse validateCommand(String[] userCommand) {
        try {
            System.out.println(userCommand[0]);
            Executable command = commandManager.getCommands().get(userCommand[0]);
            if (command == null) {
                return new ExecutionResponse(false, new AnswerString("Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'."));
            } else {
                ArgumentValidator argumentValidator = command.getArgumentValidator();
                return argumentValidator.validate(userCommand[1].trim(), command.getName());
            }
        } catch (NullPointerException e) {
            return new ExecutionResponse(false, new AnswerString("Введено недостаточно аргументов для выполнения последней команды!"));
        }
    }



    /**
     * Запускает команду.
     * @param userCommand команда для запуска
     * @return код завершения
     */
    public ExecutionResponse launchCommand(String[] userCommand, Product product, User user) {
        ExecutionResponse<?> validateStatus = validateCommand(userCommand);
        if (validateStatus.getExitCode()) {
            var command = commandManager.getCommands().get(userCommand[0]);
            Server.logger.info("Выполнение команды '" + userCommand[0] + "'");
            if (AskingCommand.class.isAssignableFrom(command.getClass())) {
                return ((AskingCommand<?>) command).execute(userCommand[1], product, user);
            } else {
                return command.execute(userCommand[1], user);
            }
        }else {
            return new ExecutionResponse(false, validateStatus.getAnswer());
        }
    }
}