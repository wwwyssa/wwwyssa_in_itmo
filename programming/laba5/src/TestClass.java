import java.util.ArrayList;
import java.util.List;

public class TestClass {
    /*public static void print1(List<? extends String> list) {
        list.add("Hello 1!");
        System.out.println(list.get(0));
    }*/

    public static void print2(List<? super String> list) {
        list.add("Hello 2!");
        System.out.println(list.get(0));
    }

    public static void main(String[] args) {
        List<String> list = List.of("str1");
        //TestClass.print1(list);
        TestClass.print2(list);
    }

}