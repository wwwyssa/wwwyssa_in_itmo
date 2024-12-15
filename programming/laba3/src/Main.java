import enums.Emotion;
import enums.Sex;
import people.*;
import performance.Act;
import performance.Performance;
import stories.FirstStory;
import stories.SecondStory;
import stories.Start;
import stories.Story;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Hero knopochka = new Hero("Кнопочка", 7, Sex.FEMALE, (int) (Math.random() * 100));
        Hero neznaika = new Hero("Незнайка", 6, Sex.MALE, (int) (Math.random() * 100));

        Start start = new Start(knopochka, neznaika);
        start.start();

        knopochka.say("Концерт между тем продолжается");

        Performance performance = new Performance();

        ArrayList<Human> magicians = new ArrayList<>() {{
            add(new Magician("Шаман", 16, Sex.MALE));
            add(new Magician("Пугна", 10, Sex.FEMALE));
            add(new Magician("Котел", 112, Sex.MALE));
            add(new Magician("Варлок", 18, Sex.MALE));
        }};

        ArrayList<Human>  gymnasts = new ArrayList<>() {{
            add(new Gymnast("Фантом Лансер", 16, Sex.MALE));
            add(new Gymnast("Фантомка", 10, Sex.FEMALE));
            add(new Gymnast("Сларк", 112, Sex.MALE));
            add(new Gymnast("Снайпер", 18, Sex.MALE));
        }};

        ArrayList<Human> dancers = new ArrayList<>() {{
            add(new Dancer("Фиенд", 16, Sex.MALE));
            add(new Dancer("Лион", 100, Sex.MALE));
            add(new Dancer("Акс", 25, Sex.MALE));
            add(new Dancer("Алхимик", 40, Sex.MALE));
        }};

        ArrayList<Human> clowns = new ArrayList<>() {{
            add(new Clown("Течис", 50, Sex.MALE));
            add(new Clown("Тинкер", 16, Sex.MALE));
            add(new Clown("Мипо", 4, Sex.MALE));
            add(new Clown("Хускар", 21, Sex.MALE));
        }};

        Act firstAct = new Act(magicians);
        Act secondAct = new Act(gymnasts);
        Act thirdAct = new Act(dancers);
        Act fourthAct = new Act(clowns);
        performance.addActs(firstAct, secondAct, thirdAct, fourthAct);
        performance.start();

        if (performance.getFunny() >= 1000){
            knopochka.setEmotion(Emotion.HAPPY);
        }
        Story story;
        if (knopochka.getEmotion() == Emotion.HAPPY) {
             System.out.println("Кнопочке понравилось представление, у нее поднялось настроение");
             story = new FirstStory(knopochka, neznaika);
        } else{
            System.out.println("Кнопочка даже не улыбнулась, глядя на представление.");
            story = new SecondStory(knopochka, neznaika);
        }
        story.start();
    }
}