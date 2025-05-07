package com.wwwyssa.lab6.client.util;

import java.util.Scanner;


/**
* Консольный интерфейс для операций ввода и вывода.
* Предоставляет методы для вывода сообщений, чтения ввода и вывода сообщений об ошибках.
 */
public interface Console {
    void print(Object obj);
    void println(Object obj);
    String input();
    void printError(Object obj);
}
