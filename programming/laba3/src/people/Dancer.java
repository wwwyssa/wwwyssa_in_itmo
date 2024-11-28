package people;

import enums.Sex;

public class Dancer extends Human {
    public Dancer(String name, int age, Sex sex) {
        super(name, age, sex);
    }

    @Override
    public String toString() {
        return "Танцор " + super.getName();
    }
    @Override
    public Action action(){
        return new Action("Выступает Танцор по имени " + super.getName(), (int) (Math.random() * 100));
    }

    @Override
    public String getProfession(){
        return "Танцор";
    }

}
