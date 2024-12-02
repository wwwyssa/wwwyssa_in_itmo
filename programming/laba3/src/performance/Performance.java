package performance;

import performance.Act;

import java.util.ArrayList;



public class Performance {
    private ArrayList<Act> acts;
    private int currentActIndex;
    private int funny;
    public Performance() {
        this.acts = new ArrayList<>();
        this.currentActIndex = 0;
        this.funny = 0;
    }

    public void addAct(Act act) {

        acts.add(act);
    }

    public void addActs(Act... inp_acts) {
        for (Act act : inp_acts) {
            acts.add(act);
        }
    }

    public void start() {
        System.out.println("Представление продолжается!");
        currentActIndex = 0;
        nextAct();
    }

    public int getFunny(){
        return funny;
    }

    public void nextAct() {
        if (currentActIndex < acts.size()) {
            acts.get(currentActIndex).perform();
            funny += acts.get(currentActIndex).getFunny();
            currentActIndex++;
            System.out.println("Следующий номер! ");
            nextAct();
        } else {
            System.out.println("Представление окончено!");
        }
    }
}