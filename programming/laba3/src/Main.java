import enums.Sex;
import people.*;
import places.Scene;
import places.SpectatorSeats;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Hero knopochka = new Hero("Кнопочка", 7, Sex.FEMALE);
        Hero neznaika = new Hero("Незнайка", 6, Sex.MALE);
        Hero knopochka2 = new Hero("Кнопочка", 5, Sex.FEMALE);

        if (knopochka.punch(neznaika)) {
            System.out.println("И сделала это так сильно, что он упал.");
        } else{
            System.out.println("Но вовремя сдержалась и, отвернулась от него");
        }
        knopochka.say("Концерт между тем продолжается");
        Scene scene = new Scene(new ArrayList<>());
        SpectatorSeats spectatorSeats = new SpectatorSeats(new ArrayList<>());
        spectatorSeats.addHuman(neznaika);
        spectatorSeats.addHuman(knopochka);
        Dancer dancer = new Dancer("Иван", 16, Sex.MALE);
        scene.addHuman(dancer);
        scene.addHuman(neznaika);
        for (Human human : scene.getHumans()) {
            Action action = human.action();
            System.out.println(action.description());
            knopochka.addSurprise(action.surprise());
        }
        ArrayList<Human> dancers = new ArrayList<Human>();
        dancers.add(new Dancer("Иван", 15, Sex.MALE));
        dancers.add(new Dancer("Шпунтик", 16, Sex.MALE));
        dancers.add(new Dancer("Ромашка", 17, Sex.FEMALE));
        scene.addHuman(dancers);
        for(Human human: scene.getHumans()){
            Action action = human.action();
            System.out.println(action.description());
            knopochka.addSurprise(action.surprise());
        }


    }
}