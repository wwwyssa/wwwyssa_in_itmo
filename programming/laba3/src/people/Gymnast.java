package people;

import enums.Sex;

public class Gymnast extends Human {
    public Gymnast(String name, int age, Sex sex) {
        super(name, age, sex);
    }

    @Override
    public String toString() {
        return "Гимнаст " + super.getName();
    }

    @Override
    public Action action(){
        return new Action("Выступает Гимнаст по имени " + super.getName() , (int) (Math.max(0, -20 + Math.random() * 100)));
    }

    @Override
    public String getProfession(){
        return "Гимнаст";
    }

}
