package com.wwwyssa.lab6.client.util;

import java.util.Scanner;

/**
 * Класс для работы с консолью.
 */
public class DefaultConsole implements Console {
    private static Scanner scanner = new Scanner(System.in);
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);

    /**
     * Выводит объект в консоль без перевода строки.
     * @param obj объект для вывода
     */
    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит объект в консоль с переводом строки.
     * @param obj объект для вывода
     */
    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Считывае�� строку из консоли.
     * @return считанная строка
     */
    @Override
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
    @Override
    public void printError(Object obj) {
        System.err.println(obj);
    }

    public boolean hasNextLine(){
        return scanner.hasNextLine();
    }


    public void nextLine(){
        scanner.nextLine();
    }
    /**
     * Возвращает сканнер для работы с файлом.
     * @return сканнер для работы с файлом
     */

    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }

}