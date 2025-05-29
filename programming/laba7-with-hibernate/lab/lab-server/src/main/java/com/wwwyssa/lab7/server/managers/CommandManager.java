package com.wwwyssa.lab7.server.managers;

import com.wwwyssa.lab7.server.util.Executable;

import java.util.ArrayList;
import java.util.LinkedHashMap;



public class CommandManager {
    private  LinkedHashMap<String, Executable> commands = new LinkedHashMap<>();
    private final ArrayList<String> commandHistory = new ArrayList<>();


    public CommandManager() {}
    /**
     * Регистрирует команду.
     * @param commandName Имя команды.
     * @param command Объект команды.
     */
    public void register(String commandName, Executable command) {
        commands.put(commandName, command);
    }

    /**
     * @return словарь команд.
     */
    public LinkedHashMap<String, Executable> getCommands() {
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