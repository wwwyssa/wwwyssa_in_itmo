package stories;

import enums.Emotion;
import people.Hero;

public class SecondStory extends Story {
    private Hero firstActor;
    private Hero secondActor;
    final private String storyName = "История два";

    public SecondStory(Hero firstActor, Hero secondActor) {
        this.firstActor = firstActor;
        this.secondActor = secondActor;
    }

    @Override
    public void start() {
        System.out.println("Начало второй истории");
        this.firstAction();
    }


    public void firstAction(){
        Emotion emotion = this.firstActor.getEmotion();
        System.out.println(this.firstActor.getName() + " " + this.firstActor.getEmotion().getDescription(this.firstActor) + " на " + this.secondActor.getName());
        System.out.println("Настроение у нее было испорчено, и выступления артистов уже не доставляли никакого удовольствия.");
        this.secondAction();
    }

    public void secondAction(){
        if (this.secondActor.getEmotion() != Emotion.HAPPY){
            this.secondActor.setEmotion(Emotion.HAPPY);
        }
        System.out.println("Зато " + this.secondActor.getName() + " смеялся до упадую");
        if (Math.random() < 0.5){
            System.out.println(this.secondActor + " смеялся так сильно, что упал со стула и ударился");
            this.secondActor.setEmotion(Emotion.SAD);
            this.secondActor.say("Ай как больно!");
            System.out.println(this.secondActor + " набил на голове шишку ");
        }
    }
}
