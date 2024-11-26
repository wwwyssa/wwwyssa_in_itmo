package people;

import enums.Sex;

public class Magician extends Human{
    public Magician(String name, int age, Sex sex) {
        super(name, age, sex);
    }

    @Override
    public String toString() {
        return "Magician {" + "name='" + this.getName() + '\'' + ", age=" + this.getAge() + '}';
    }

    @Override
    public Action action(){
        return new Action("Выступает волшебник со своим номером!", (int) (Math.random() * 100));
    }
}
