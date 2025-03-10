package utils;

/**
 * Класс, представляющий ответ выполнения.
 */
public class ExecutionResponse {
    private boolean exitCode;
    private Object answer;

    /**
     * Конструктор для создания объекта ExecutionResponse.
     * @param code код завершения
     * @param s сообщение
     */
    public ExecutionResponse(boolean code, Object s) {
        exitCode = code;
        answer = s;
    }

    /**
     * Конструктор для создания объекта ExecutionResponse с кодом завершения по умолчанию.
     * @param s сообщение
     */
    public ExecutionResponse(String s) {
        exitCode = true;
        answer = s;
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
    public String getAnswer() { return answer.toString(); }

    /**
     * Возвращает строковое представление объекта ExecutionResponse.
     * @return строковое представление объекта
     */
    public String toString() { return String.valueOf(exitCode) + ";" + answer; }
}