package utils;

/**
 * Интерфейс для работы с консолью.
 */
public interface ConsoleInterface {
    /**
     * Выводит объект в консоль без перевода строки.
     * @param obj объект для вывода
     */
    void print(Object obj);

    /**
     * Выводит объект в консоль с переводом строки.
     * @param obj объект для вывода
     */
    void println(Object obj);

    /**
     * Выводит объект в консоль как ошибку.
     * @param obj объект для вывода
     */
    void printError(Object obj);

    /**
     * Считывает строку из консоли.
     * @return считанная строка
     */
    String input();
}