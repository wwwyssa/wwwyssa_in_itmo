package commands;

import managers.CollectionManager;
import utils.Console;
import utils.ExecutionResponse;

public class AverageOfManufactureCost extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public AverageOfManufactureCost(Console console, CollectionManager collectionManager){
        super("average_of_manufacture_cost", "вывести среднее значение поля manufactureCost для всех элементов коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(String[] arguments){
        if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");

        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse(false, "Коллекция пуста!");

        double sum = 0;
        for (var product : collectionManager.getCollection().values()) {
            sum += product.getManufactureCost();
        }

        return new ExecutionResponse(true, "Среднее значение поля manufactureCost для всех элементов коллекции: " + sum / collectionManager.getCollection().size());
    }
}
