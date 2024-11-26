import people.*;
import enums.*;
import places.*;

public class Main {
    public static void main(String[] args) {
        Magician p = new Magician("1", 2, Sex.MALE);
        System.out.println(p);
        System.out.println(p.getAge());
        System.out.println(p.action().description());
    }
}