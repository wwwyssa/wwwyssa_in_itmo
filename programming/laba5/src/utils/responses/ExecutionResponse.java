package utils.responses;

import models.Product;
import java.util.ArrayList;

/**
 * Класс, представляющий ответ выполнения.
 * @param <T> тип ответа
 */
public class ExecutionResponse<T extends ValidAnswer> {
    private boolean exitCode;
    private T answer;

    /**
     * Конструктор для создания объекта ExecutionResponse.
     * @param code код завершения
     * @param answer ответ
     */
    public ExecutionResponse(boolean code, T answer) {
        this.exitCode = code;
        this.answer = answer;
    }

    /**
     * Конструктор для создания объекта ExecutionResponse с кодом завершения по умолчанию.
     * @param answer ответ
     */
    public ExecutionResponse(T answer) {
        this.exitCode = true;
        this.answer = answer;
    }

    /**
     * Возвращает код завершения.
     * @return код завершения
     */
    public boolean getExitCode() {
        return exitCode;
    }

    /**
     * Возвращает ответ.
     * @return ответ
     */
    public T getAnswer() {
        return answer;
    }

    /**
     * Возвращает строковое представление объекта ExecutionResponse.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return String.valueOf(exitCode) + ";" + answer;
    }
}