package places;

import people.Human;

import java.util.ArrayList;

public abstract class Place {
    private ArrayList<Human> humans = new ArrayList<Human>();

    public Place(ArrayList<Human> humans) {
        this.humans = humans;
    }

    public ArrayList<Human> getHumans() {
        return humans;
    }


    public void addHuman(Human human) {
        this.humans.add(human);
    }

    public void addHuman(ArrayList<Human> humans){
        this.humans.addAll(humans);
    }
}
