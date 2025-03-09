package commands;

import managers.CollectionManager;
import managers.CommandManager;
import utils.Console;
import utils.ExecutionResponse;
import utils.ReplaceIfLower;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Класс команды для выполнения скрипта из указанного файла.
 */
public class ExecuteScript extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    private CommandManager commandManager = null;

    /**
     * Конструктор класса ExecuteScript.
     * @param console Консоль для взаимодействия с пользователем.
     * @param collectionManager Менеджер коллекции.
     */
    public ExecuteScript(Console console, CollectionManager collectionManager) {
        super("execute_script", "считать и исполнить скрипт из указанного файла", 1);
        this.console = console;
        this.collectionManager = collectionManager;
        this.commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new Update(console, collectionManager));
            register("remove_key", new RemoveById(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("exit", new Exit(console));
            register("remove_greater", new RemoveGreater(console, collectionManager));
            register("replace_if_lower", new ReplaceIfLower(console, collectionManager));
            register("remove_greater_key", new RemoveGreaterKey(console, collectionManager));
            register("min_by_name", new MinByName(console, collectionManager));
            register("print_field_ascending_part_number", new PrintFieldAscendingPartNumber(console, collectionManager));
        }};
    }

    /**
     * Выполняет команду с указанными аргументами.
     * @param args Аргументы команды.
     * @return Результат выполнения команды.
     */
    @Override
    public ExecutionResponse execute(String[] args) {
        File file = null;
        try {
            file = new File("src\\" + args[1].trim());
        } catch (IndexOutOfBoundsException e) {
            console.println("Неправильное количество аргументов!\nИспользование: '" + getName() + " {file}'");
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + " {file}'");
        }
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                while (scanner.hasNextLine()) {
                    String lineInp = scanner.nextLine();
                    if (lineInp.isEmpty()) continue;
                    String[] line = (lineInp + " ").split(" ", 2);
                    Command command = commandManager.getCommands().get(line[0]);
                    if (command == null) {
                        console.println("Команда '" + line[0] + "' не найдена. Наберите 'help' для справки");
                        return new ExecutionResponse(false, "Команда '" + line[0] + "' не найдена. Наберите 'help' для справки");
                    }
                    try {
                        ExecutionResponse commandStatus = command.execute(line);
                        if (commandStatus.getMessage().equals("add")){
                            boolean flag = true;
                            //todo add;
                        }
                        if (commandStatus.getMessage().equals("exit")) return new ExecutionResponse("exit");
                        console.println(commandStatus.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Не удалось выполнить команду" + ex.getMessage());
                        return new ExecutionResponse(false, "Не удалось выполнить команду");
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            console.println("Файл не найден!");
            return new ExecutionResponse(false, "Файл не найден!");
        } catch (Exception e) {
            console.println("Что-то пошло не так" + e.getMessage());
            return new ExecutionResponse(false, "Что-то пошло не так");
        }
        return new ExecutionResponse(true, "Скрипт успешно выполнен");
    }
}