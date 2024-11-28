package people;

import enums.Sex;

public class Clown extends Human {
    public Clown(String name, int age, Sex sex) {
        super(name, age, sex);
    }


    @Override
    public String toString() {
        return "Клоун " + super.getName();
    }



    @Override
    public Action action(){
        return new Action("Выступает клоун " + super.getName() + " Хи-хи ха-ха", (int) (Math.max(100, 20 + Math.random() * 100)));
    }

    @Override
    public String getProfession() {
        return "Клоун";
    }
}
