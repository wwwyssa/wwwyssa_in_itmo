import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.EventHandler;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Stream.of("rat", "ox", "tiger", "rabbit", "dragon", "snake")
                .filter(s -> s.length() > 4)
                .limit(2)
                .sorted()
                .forEachOrdered(System.out::print);

        TextArea myControl = new TextArea("Return of the Jedi");
        myControl.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                this.setBackground(Color.YELLOW);
            }

            private void setBackground(Color yellow) {
            }
        });


    }

    public static String del(String input) {
        if (input == null || input.length() < 2) {
            return input; // Возвращаем строку без изменений, если она слишком короткая
        }
        return input.substring(0, input.length() - 2) + input.charAt(input.length() - 1);
    }

}