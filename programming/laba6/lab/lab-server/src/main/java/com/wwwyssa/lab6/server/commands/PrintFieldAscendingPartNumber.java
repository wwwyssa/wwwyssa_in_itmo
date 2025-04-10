package commands;

import managers.CollectionManager;
import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;
import utils.responses.ListAnswer;
import utils.responses.ValidAnswer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Product;

/**
     * Класс команды для вывода значений поля partNumber всех элементов в порядке возрастания.
     */
    public class PrintFieldAscendingPartNumber extends Command {
        private final DefaultConsole defaultConsole;
        private final CollectionManager collectionManager;

        /**
         * Конструктор класса PrintFieldAscendingPartNumber.
         * @param defaultConsole Консоль для взаимодействия с пользователем.
         * @param collectionManager Менеджер коллекции.
         */
        public PrintFieldAscendingPartNumber(DefaultConsole defaultConsole, CollectionManager collectionManager) {
            super("print_field_ascending_part_number", "вывести значения поля partNumber всех элементов в порядке возрастания", 0);
            this.defaultConsole = defaultConsole;
            this.collectionManager = collectionManager;
        }

        /**
         * Выполняет команду с указанными аргументами.
         *
         * @param arguments Аргументы команды.
         * @return Результат выполнения команды.
         */
        @Override
        public ExecutionResponse<ListAnswer> innerExecute(String[] arguments) {
            String tmpString = "";
            ArrayList<Product> tmpList = new ArrayList<>();
            for (var product : collectionManager.getCollection().values()) {
                tmpList.add(product);
            }
            tmpList.sort(Product::compareTo);
            return new ExecutionResponse<>(true, new ListAnswer(tmpList));
        }
    }