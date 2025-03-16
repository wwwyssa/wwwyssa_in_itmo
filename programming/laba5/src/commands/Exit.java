package commands;

import utils.console.DefaultConsole;
import utils.responses.AnswerString;
import utils.responses.ExecutionResponse;

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
    public ExecutionResponse innerExecute(String[] arguments) {
        return new ExecutionResponse(new AnswerString("exit"));
    }
}