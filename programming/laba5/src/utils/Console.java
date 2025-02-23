package utils;

import java.util.Scanner;

/**
 * Класс для работы с консолью.
 */
public class Console {
    private static Scanner scanner = new Scanner(System.in);
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);

    /**
     * Выводит объект в консоль без перевода строки.
     * @param obj объект для вывода
     */
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит объект в консоль с переводом строки.
     * @param obj объект для вывода
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Считывае�� строку из консоли.
     * @return считанная строка
     */
    public String input() {
        if (fileScanner != null) {
            return fileScanner.nextLine();
        }
        return scanner.nextLine();
    }

    /**
     * Выводит объект в консоль как ошибку.
     * @param obj объект для вывода
     */
    public void printError(Object obj) {
        System.err.println(obj);
    }

    /**
     * Возвращает сканнер для работы с файлом.
     * @return сканнер для работы с файлом
     */
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    /**
     * Возвращает сканнер для работы с консолью.
     * @return сканнер для работы с консолью
     */
    public void selectConsoleScanner() {
        fileScanner = null;
    }

    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }

}