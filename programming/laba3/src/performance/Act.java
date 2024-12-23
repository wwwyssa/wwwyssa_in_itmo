package performance;

import people.Action;
import people.Human;

import java.util.ArrayList;

public class Act {
    private ArrayList<Human> actors;
    private int funny;

    public Act(ArrayList<Human> actors) {
        this.actors = actors;
    }

    public ArrayList<Human> getActors() {
        return actors;
    }

    public int getFunny() {
        return funny;
    }

    public void perform() {
        for (Human human : actors) {
            human.setLocation("Stage");
            Action action = human.action();
            System.out.println(action.description());
            this.funny += action.surprise();
        }
    }
}