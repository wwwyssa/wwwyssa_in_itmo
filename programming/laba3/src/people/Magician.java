package people;

import enums.Sex;

public class Magician extends Human{
    public Magician(String name, int age, Sex sex) {
        super(name, age, sex);
    }

    @Override
    public String toString() {
        return "Волшебник " + super.getName();
    }

    @Override
    public String getProfession(){
        return "Волшебник";
    }

    @Override
    public Action action(){
        return new Action("Выступает волшебник по имени " + super.getName() + " со своим номером!", (int) (Math.random() * 100));
    }
}
