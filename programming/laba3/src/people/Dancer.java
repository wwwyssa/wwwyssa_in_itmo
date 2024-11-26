package people;

import enums.Sex;

public class Dancer extends Human {
    public Dancer(String name, int age, Sex sex) {
        super(name, age, sex);
    }

    @Override
    public String toString() {
        return "Dancer {" + "name='" + this.getName() + '\'' + ", age=" + this.getAge() + '}';
    }

    @Override
    public Action action(){
        return new Action("Выступает Танцор", (int) (Math.random() * 100));
    }

}
