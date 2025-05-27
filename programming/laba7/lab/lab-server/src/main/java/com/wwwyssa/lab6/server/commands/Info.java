package com.wwwyssa.lab6.server.commands;



import com.wwwyssa.lab6.common.util.User;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.common.validators.NoArgumentsValidator;
import com.wwwyssa.lab6.server.managers.CollectionManager;

import java.time.LocalDateTime;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command<NoArgumentsValidator> {
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции", 0, new NoArgumentsValidator());
        this.collectionManager = collectionManager;
    }



    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse innerExecute(String arguments, User user) {
        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        String s = "Сведения о коллекции:\n";
        s+=" Тип: " + collectionManager.getCollection().getClass().toString()+"\n";
        s+=" Количество элементов: " + collectionManager.getCollection().size()+"\n";
        s+=" Дата последнего сохранения: " + lastSaveTimeString+"\n";
        s+=" Дата последней инициализации: " + lastInitTimeString;
        return new ExecutionResponse<>(new AnswerString(s));
    }
}