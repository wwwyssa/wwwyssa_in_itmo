package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.ExecutionResponse;

import java.util.ArrayList;

public class PrintFieldAscendingPartNumber extends Command{
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;


    public PrintFieldAscendingPartNumber(DefaultConsole defaultConsole, CollectionManager collectionManager){
        super("print_field_ascending_part_number", "вывести значения поля partNumber всех элементов в порядке возрастания", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(String[] arguments) {
        /*if (!arguments[1].isEmpty()) {console.println("Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        }*/
        ExecutionResponse response = validate(arguments);
        if (!response.getExitCode()) return response;

        if (collectionManager.getCollection().isEmpty()) {
            defaultConsole.println("Коллекция пуста!");
            return new ExecutionResponse(false, "Коллекция пуста!");
        }

        ArrayList<String> partNumbers = new ArrayList<>();
        for (var product : collectionManager.getCollection().values()) {
            partNumbers.add(product.getPartNumber());
        }
        partNumbers.sort(String::compareTo);
        return new ExecutionResponse(true, partNumbers.toString());
    }
}
