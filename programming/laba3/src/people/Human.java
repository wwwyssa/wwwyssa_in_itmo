package people;

import enums.Sex;

public abstract class Human implements People {
    private String name;
    private int age;
    private Sex sex;

    public Human(String name, int age, Sex sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean isMale() {
        return sex == Sex.MALE;
    }


    public Action action(){
        return new Action("Базовое действие человека", 0);
    }

    @Override
    public String toString() {
        return "Human {" + "name='" + name + '\'' + ", age=" + age + '}';
    }
}
