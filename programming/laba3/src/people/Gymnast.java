package people;

import enums.Sex;

public class Gymnast extends Human {
    public Gymnast(String name, int age, Sex sex) {
        super(name, age, sex);
    }

    @Override
    public String toString() {
        return "Gymnast {" + "name='" + this.getName() + '\'' + ", age=" + this.getAge() + '}';
    }

    @Override
    public Action action(){
        return new Action("Выступает Гимнаст", (int) (Math.max(0, -20 + Math.random() * 100)));
    }

}
