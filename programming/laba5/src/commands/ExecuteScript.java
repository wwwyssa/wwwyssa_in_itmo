package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import managers.CollectionManager;
import managers.CommandManager;
import utils.console.DefaultConsole;
import utils.console.FileConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;
import utils.responses.ValidAnswer;

/**
 * Класс команды для выполнения скрипта из указанного файла.
 */
public class ExecuteScript extends Command {
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;
    private CommandManager commandManager = null;
    private ArrayList<String> scriptStack = new ArrayList<>();
    /**
     * Конструктор класса ExecuteScript.
     * @param defaultConsole Консоль для взаимодействия с пользователем.
     * @param collectionManager Менеджер коллекции.
     */
    public ExecuteScript(DefaultConsole defaultConsole, CollectionManager collectionManager) {
        super("execute_script", "считать и исполнить скрипт из указанного файла", 1);
        this.collectionManager = collectionManager;
        this.defaultConsole = defaultConsole;
        this.commandManager = new CommandManager() {{
            register("help", new Help(defaultConsole, this));
            register("info", new Info(defaultConsole, collectionManager));
            register("show", new Show(defaultConsole, collectionManager));
            register("update", new Update(defaultConsole, collectionManager));
            register("remove_key", new RemoveById(defaultConsole, collectionManager));
            register("save", new Save(defaultConsole, collectionManager));
            register("exit", new Exit(defaultConsole));
            register("remove_greater", new RemoveGreater(defaultConsole, collectionManager));
            register("replace_if_lower", new ReplaceIfLower(defaultConsole, collectionManager));
            register("remove_greater_key", new RemoveGreaterKey(defaultConsole, collectionManager));
            register("min_by_name", new MinByName(defaultConsole, collectionManager));
            register("print_field_ascending_part_number", new PrintFieldAscendingPartNumber(defaultConsole, collectionManager));
            register("average_of_manufacture_cost", new AverageOfManufactureCost(defaultConsole, collectionManager));
            register("clear", new Clear(defaultConsole, collectionManager));
        }};
    }

    /**
     * Выполняет команду с указанными аргументами.
     * @param args Аргументы команды.
     * @return Результат выполнения команды.
     */
    @Override
    public ExecutionResponse<ValidAnswer<String>> innerExecute(String[] args) {
        File file = null;
        try {
            file = new File(args[1].trim());
            scriptStack.add(args[1].trim());
            FileConsole fileConsole = new FileConsole(file);
            commandManager.register("add", new Add(fileConsole, collectionManager));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Неправильное количество аргументов!\nИспользование: '" + getName() + " {file}'");
            return new ExecutionResponse<ValidAnswer<String>>(false, new AnswerString("Неправильное количество аргументов!\nИспользование: '" + getName() + " {file}'"));
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
            return new ExecutionResponse<ValidAnswer<String>>(false, new AnswerString("Файл не найден!"));
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
                        if (line[0].equals("execute_script")) {
                            if (scriptStack.contains(line[1].trim())) {
                                defaultConsole.println("Обнаружен циклический вызов скрипта '" + line[1] + "'. Пропуск...");
                                continue;
                            } else {
                                scriptStack.add(line[1].trim());
                                execute(line);
                                scriptStack.remove(line[1].trim());
                            }
                        } else{
                            defaultConsole.println("Команда '" + line[0] + "' не найдена. Наберите 'help' для справки");
                            return new ExecutionResponse(false, new AnswerString("Команда '" + line[0] + "' не найдена. Наберите 'help' для справки"));
                        }

                    } else{
                        try {
                            ExecutionResponse commandStatus = command.execute(line);
                            if (command.getName().equals("add {Product}")) {
                                for(int i = 0; i < 14; i++) {
                                    if (scanner.hasNextLine()) scanner.nextLine();
                                }
                            }

                            if (commandStatus.getAnswer().getAnswer().equals("exit")) return new ExecutionResponse<ValidAnswer<String>>(new AnswerString("exit"));
                            defaultConsole.println(commandStatus.getAnswer().getAnswer());
                        } catch (Exception ex) {
                            System.out.println("Не удалось выполнить команду" + ex.getMessage());
                            return new ExecutionResponse<ValidAnswer<String>>(false, new AnswerString("Не удалось выполнить команду"));
                        }
                    }

                }
            }
            scriptStack.remove(args[1].trim());
            scanner.close();
        } catch (Exception e) {
            System.out.println("Что-то пошло не так" + e.getMessage());
            return new ExecutionResponse(false, new AnswerString("Что-то пошло не так"));
        }
        return new ExecutionResponse(true,new AnswerString("Скрипт выполнен"));
    }
}