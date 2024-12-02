package people;

import enums.Sex;

import java.util.Objects;

public abstract class Human implements People {
    private String name;
    private int age;
    private Sex sex;
    private int power = 0;
    private String location;
    public Human(String name, int age, Sex sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Human(String name, int age, Sex sex, int power) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.power = power;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getPower() {
        return this.power;
    }


    @Override
    public boolean isMale() {
        return sex == Sex.MALE;
    }


    public Action action(){
        return new Action("Базовое действие человека", 0);
    }

    abstract public String getProfession();

    @Override
    public String toString() {
        return "Human {" + "name='" + name + '\'' + ", age=" + age + '}';
    }

    @Override
    public void say(String text){
        System.out.println(this.name + " " + (this.isMale() ? "сказал: " : "сказала: ") + text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human person = (Human) o;
        return Objects.equals(name, person.name) && Objects.equals(age, person.age) && Objects.equals(sex, person.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, sex);
    }


}
