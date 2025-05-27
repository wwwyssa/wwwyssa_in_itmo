package com.wwwyssa.lab6.client.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Класс для чтения данных из файла, реализующий интерфейс Console. Заглушает стандартный поток вывода.
 */
public class FileConsole implements Console {
    private final Scanner scanner;
    /**
     * Конструктор для создания FileConsole.
     * @param input Буферизированный ридер для чтения данных из файла.
     */
    public FileConsole(File input) throws FileNotFoundException {
        this.scanner = new Scanner(input);
    }


    /**
     * Печатает сообщение об ошибке в стандартный поток ошибок.
     * @param obj Сообщение об ошибке.
     */
    @Override
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    /**
     * Считывает строку из файла.
     * @return Считанная строка.
     * @throws IllegalArgumentException Если произошла ошибка ввода-вывода.
     */
    @Override
    public String input() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            throw new IllegalArgumentException("ERROR");
        }
    }

    @Override
    public void print(Object obj) {}

    @Override
    public void println(Object obj) {}

    public void skipLines(int lines) {
        for (int i = 0; i < lines; i++) {
            scanner.nextLine();
        }
    }

}