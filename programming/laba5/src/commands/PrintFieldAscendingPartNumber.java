package commands;

    import managers.CollectionManager;
    import utils.console.DefaultConsole;
    import utils.responses.ExecutionResponse;

    import java.util.ArrayList;

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
         * @param arguments Аргументы команды.
         * @return Результат выполнения команды.
         */
        @Override
        public ExecutionResponse innerExecute(String[] arguments) {
            if (collectionManager.getCollection().isEmpty()) {
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