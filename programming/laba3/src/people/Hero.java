package people;

import enums.Emotion;
import enums.Sex;

public class Hero extends Human {
    public Hero(String name, int age, Sex sex) {
        super(name, age, sex);
    }
    private int surprise = 0;
    private Emotion emotion = Emotion.DEFAULT;
    @Override
    public String toString() {
        return "Персонаж " + super.getName();
    }

    @Override
    public String getProfession(){
        return "Герой";
    }

    public int getSurprise() {
        return surprise;
    }

    public void addSurprise(int surprise) {
        this.surprise +=  surprise;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public boolean punch(Human human) {
        if (Math.random() < 0.5) {
            System.out.println(this.getName() +  (this.isMale() ? " хотел " : " хотела ") + "ударить " + human.getName());
            return false;
        }
        System.out.println(this.getName() +  (this.isMale() ? " хотел " : " хотела ") + "ударить " + human.getName() + " и " +
                (this.isMale() ? "сделал " : "сделала ") + "это");
        return true;
    }





}