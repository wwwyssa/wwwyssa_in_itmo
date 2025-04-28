package com.wwwyssa.lab6.server.managers;

import com.wwwyssa.lab6.server.commands.Command;

import java.util.ArrayList;
import java.util.LinkedHashMap;



public class CommandManager {
    private  LinkedHashMap<String, Command> commands = new LinkedHashMap<>();
    private final ArrayList<String> commandHistory = new ArrayList<>();


    public CommandManager() {}
    /**
     * Регистрирует команду.
     * @param commandName Имя команды.
     * @param command Объект команды.
     */
    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * @return словарь команд.
     */
    public LinkedHashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * Добавляет команду в историю.
     * @param command Команда.
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }
}