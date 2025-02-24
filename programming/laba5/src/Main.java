import commands.*;

import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;

import models.Product;

import utils.Console;
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
        /*if (collectionManager.loadCollection()) {
            System.exit(1);
        }*/

        var commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new Update(console, collectionManager));
            register("remove_by_id", new RemoveById(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("clear", new Clear(console, collectionManager));
        }};

        new Runner(console, commandManager).interactiveMode();
    }
}
