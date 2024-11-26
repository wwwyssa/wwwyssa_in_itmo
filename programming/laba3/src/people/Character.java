package people;

import enums.Sex;

public class Character extends Human {
    public Character(String name, int age, Sex sex) {
        super(name, age, sex);
    }
    private int surprise = 0;

    @Override
    public String toString() {
        return "Character {" + "name='" + this.getName() + '\'' + ", age=" + this.getAge() + '}';
    }

    public int getSurprise() {
        return surprise;
    }

    public void addSurprise(int surprise) {
        this.surprise +=  surprise;
    }



}
