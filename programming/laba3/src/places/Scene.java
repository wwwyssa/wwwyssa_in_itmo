package places;

import people.Dancer;
import people.Human;

import java.util.ArrayList;

public class Scene extends Place {
    public Scene(ArrayList<Human> humans) {
        super(humans);
    }

    public ArrayList<Human> getHumans() {
        System.out.println("На сцене выступают: ");
        for(Human human : super.getHumans()) {
            System.out.println(human);
        }
        return (ArrayList<Human>) super.getHumans();
    }

    @Override
    public void addHuman(Human human) {
        if (!super.getHumans().isEmpty()) {
            System.out.println("Следующим будет выступать " + human);
            super.addHuman(human);
        } else{
            System.out.println("На сцену выходит " + human);
            super.addHuman(human);
        }
    }

    public void addHuman(ArrayList<Human> humans) {
        String s = "";
        for(Human human : humans) {
            s += human + " ";
        }
        super.addHuman(humans);
        System.out.println("На сцену выходят " + s);
    }

}
