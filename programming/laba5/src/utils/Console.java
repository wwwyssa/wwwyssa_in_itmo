package utils;
import java.util.Scanner;

public class Console {
    private static Scanner scanner = new Scanner(System.in);
    private static Scanner fileScanner = null;
    public void print(Object obj) {
        System.out.print(obj);
    }

    public void println(Object obj) {
        System.out.println(obj);
    }

    public String input() {
        if (fileScanner != null){
            return fileScanner.nextLine();
        }
        return scanner.nextLine();
    }

    public void printError(Object obj) {
        System.err.println(obj);
    }





}
