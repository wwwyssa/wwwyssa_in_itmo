import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(2);
        list.add(3);
        list.add(0, 4);
        list.add(2);
        list.remove(1);
        for(var x: list) {
            System.out.println(x);
        }
    }
}