package commands;

import utils.DefaultConsole;
import utils.ExecutionResponse;

/**
 * Команда 'exit'. Завершает выполнение.
 * @author dim0n4eg
 */
public class Exit extends Command {
    private final DefaultConsole defaultConsole;

    public Exit(DefaultConsole defaultConsole) {
        super("exit", "завершить программу (без сохранения в файл)", 0);
        this.defaultConsole = defaultConsole;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse execute(String[] arguments) {
        //if (!arguments[1].isEmpty()) return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        ExecutionResponse response = validate(arguments);
        if (!response.getExitCode()) return response;
        return new ExecutionResponse("exit");
    }
}