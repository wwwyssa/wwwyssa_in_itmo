import commands.*;

import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;

import utils.Console;
import utils.ReplaceIfLower;
import utils.Runner;

public class Main {
    public static void main(String[] args) {
        var console = new Console();

        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        var dumpManager = new DumpManager(args[0], console);
        var collectionManager = new CollectionManager(dumpManager);
        if (!collectionManager.loadCollection()) {
            System.exit(1);
        }

        CommandManager commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new Update(console, collectionManager));
            register("remove_key", new RemoveById(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("clear", new Clear(console, collectionManager));
            register("exit", new Exit(console));
            register("remove_greater", new RemoveGreater(console, collectionManager));
            register("replace_if_lower", new ReplaceIfLower(console, collectionManager));
            register("remove_greater_key", new RemoveGreaterKey(console, collectionManager));
            register("average_of_manufacture_cost", new AverageOfManufactureCost(console, collectionManager));
            register("min_by_name", new MinByName(console, collectionManager));
            register("print_field_ascending_part_number", new PrintFieldAscendingPartNumber(console, collectionManager));
            register("execute_script", new ExecuteScript(console, collectionManager));
        }};
        new Runner(console, commandManager).run();
    }
}
