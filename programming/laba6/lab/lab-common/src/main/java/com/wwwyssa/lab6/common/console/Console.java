package com.wwwyssa.lab6.common.console;

import java.util.Scanner;

public interface Console {
    void print(Object obj);
    void println(Object obj);
    String input();
    void printError(Object obj);
}
