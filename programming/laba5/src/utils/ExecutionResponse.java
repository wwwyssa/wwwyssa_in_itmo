package utils;

/**
 * Класс, представляющий ответ выполнения.
 */
public class ExecutionResponse {
    private boolean exitCode;
    private String message;

    /**
     * Конструктор для создания объекта ExecutionResponse.
     * @param code код завершения
     * @param s сообщение
     */
    public ExecutionResponse(boolean code, String s) {
        exitCode = code;
        message = s;
    }

    /**
     * Конструктор для создания объекта ExecutionResponse с кодом завершения по умолчанию.
     * @param s сообщение
     */
    public ExecutionResponse(String s) {
        exitCode = true;
        message = s;
    }

    /**
     * Возвращает код завершения.
     * @return код завершения
     */
    public boolean getExitCode() { return exitCode; }

    /**
     * Возвращает сообщение.
     * @return сообщение
     */
    public String getMessage() { return message; }

    /**
     * Возвращает строковое представление объекта ExecutionResponse.
     * @return строковое представление объекта
     */
    public String toString() { return String.valueOf(exitCode) + ";" + message; }
}