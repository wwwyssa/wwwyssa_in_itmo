package managers;

import commands.Command;

import java.util.ArrayList;
import java.util.LinkedHashMap;



public class CommandManager {
    private final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();
    private final ArrayList<String> commandHistory = new ArrayList<>();

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
     * @return история команд.
     */
    public ArrayList<String> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Добавляет команду в историю.
     * @param command Команда.
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }
}