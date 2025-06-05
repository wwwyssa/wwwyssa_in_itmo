import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Stream.of("micro", "nano", "pico", "femto", "atto", "zepto", "yocto")
                .filter(s -> s.length() == 5)
                .map(s -> Main.del(s))
                .limit(2)
                .sorted()
                .forEachOrdered(System.out::print);
    }

    public static String del(String input) {
        if (input == null || input.length() < 2) {
            return input; // Возвращаем строку без изменений, если она слишком короткая
        }
        return input.substring(0, input.length() - 2) + input.charAt(input.length() - 1);
    }

}