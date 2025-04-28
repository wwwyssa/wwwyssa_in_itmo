package com.wwwyssa.lab6.client.util;

import java.util.Scanner;

public interface Console {
    void print(Object obj);
    void println(Object obj);
    String input();
    void printError(Object obj);
}
