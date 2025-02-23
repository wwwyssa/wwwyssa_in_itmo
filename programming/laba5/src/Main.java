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



        var dumpManager = new DumpManager("1.xml", console);
        var collectionManager = new CollectionManager(dumpManager);


        var commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new Update(console, collectionManager));
            register("remove_by_id", new RemoveById(console, collectionManager));
        }};

        new Runner(console, commandManager).interactiveMode();
    }
}
