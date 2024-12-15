package stories;

import enums.Emotion;
import people.*;

public class Start extends Story{
    private Hero firstActor;
    private Hero secondActor;

    final private String storyName = "Начало";
    public Start(Hero firstActor, Hero secondActor) {
        this.firstActor = firstActor;
        this.secondActor = secondActor;
    }

    public void start(){
        System.out.println("Начало первой истории");
        firstAction();
    }

    public void firstAction(){
        firstActor.setEmotion(Emotion.SAD);
        secondActor.setEmotion(Emotion.HAPPY);
        secondActor.say(firstActor.getName() + " в кого-то влюбилась!!!!");
        firstActor.setEmotion(Emotion.RESENTMENT);
        try{
            if (firstActor.punch(secondActor)) {
                if (secondActor.getHealth() < 60){
                    System.out.println("И сделала это так сильно, что он упал.  Здоровье " + secondActor.getName() + ' ' + secondActor.getHealth());
                    secondActor.setEmotion(Emotion.SAD);
                }else{
                    System.out.println("Но стукнула его не сильно. Здоровье " + secondActor.getName() + ' ' + secondActor.getHealth());
                }

            } else{
                System.out.println("Но вовремя сдержалась и отвернулась от него");
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
