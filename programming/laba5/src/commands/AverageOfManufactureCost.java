package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;
import utils.responses.ValidAnswer;


/**
 * Класс команды для вывода среднего значения поля manufactureCost для всех элементов коллекции.
 */
public class AverageOfManufactureCost extends Command{
    private final DefaultConsole defaultConsole;
    private final CollectionManager collectionManager;


    /**
     * Конструктор класса
     * @param defaultConsole объект консоли
     * @param collectionManager объект менеджера коллекции
     */
    public AverageOfManufactureCost(DefaultConsole defaultConsole, CollectionManager collectionManager){
        super("average_of_manufacture_cost", "вывести среднее значение поля manufactureCost для всех элементов коллекции", 0);
        this.defaultConsole = defaultConsole;
        this.collectionManager = collectionManager;
    }


    @Override
    public ExecutionResponse<ValidAnswer<String>> innerExecute(String[] arguments){
        if (collectionManager.getCollection().isEmpty()) return new ExecutionResponse<>(false, new AnswerString("Коллекция пуста!"));

        double sum = 0;
        for (var product : collectionManager.getCollection().values()) {
            sum += product.getManufactureCost();
        }

        return new ExecutionResponse<>(true, new AnswerString("Среднее значение поля manufactureCost для всех элементов коллекции: " + sum / collectionManager.getCollection().size()));
    }
}
