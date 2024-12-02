package people;

import enums.Emotion;
import enums.Sex;

public class Hero extends Human {
    private int health = 100;
    public Hero(String name, int age, Sex sex, int power) {
        super(name, age, sex, power);
    }


    private Emotion emotion = Emotion.DEFAULT;
    @Override
    public String toString() {
        return "Персонаж " + super.getName();
    }

    @Override
    public String getProfession(){
        return "Герой";
    }



    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
        System.out.println(this.getName() + " " + this.getEmotion().getDescription(this));
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int damage){
        this.setHealth(this.getHealth() - damage);
    }



    public boolean punch(Hero human) {
        if (this.getPower() == 0) {
            throw new ZeroPowerException(this.getName() + " не может ударить, так как у него нет силы!");
        }

        if ((Math.random() < 0.5) || this.getPower() < human.getPower() * 1.2) {
            System.out.println(this.getName() + (this.isMale() ? " хотел " : " хотела ") + "ударить " + human.getName());
            return false;
        }
        System.out.println(this.getName() + (this.isMale() ? " хотел " : " хотела ") + "ударить " + human.getName() + " и " +
                (this.isMale() ? "сделал " : "сделала ") + "это");
        human.decreaseHealth(this.getPower());

        return true;
    }
}


class ZeroPowerException extends RuntimeException {
    public ZeroPowerException(String message) {
        super(message);
    }

    @Override
    public String getMessage(){
        return "Ошибка героя: " +  super.getMessage();
    }
}



