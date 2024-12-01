package stories;

import enums.Emotion;
import people.*;


public class FirstStory extends Story {
    private Hero firstActor;
    private Hero secondActor;

    final private String storyName = "История один";
    public FirstStory(Hero firstActor, Hero secondActor) {
        this.firstActor = firstActor;
        this.secondActor = secondActor;
    }

    @Override
    public void start(){
        System.out.println("Начало первой истории");
        this.firstAction();
    }

    public void firstAction(){
        this.firstActor.setEmotion(Emotion.HAPPY);
        System.out.println(this.firstActor.getName() + " настолько понравилось представление, что она подумала, может быть ей простить " + this.secondActor.getName());
        System.out.println("И решила подбросить монетку");
        if (Math.random() < 0.5){
            this.firstActor.say("выпала не та сторона, что я загадала, я тебя " + this.secondActor.getName() + " не прощаю");
        } else{
            this.firstActor.say("выпала та сторона, что я загадала, я тебя " + this.secondActor.getName() + " прощаю");
        }
        this.secondActor.say("Прости, меня " + this.firstActor.getName());
        this.secondAction();
    }

    public void secondAction(){
        System.out.println("После окончания спектакля герои пошли по домам");
    }
}
