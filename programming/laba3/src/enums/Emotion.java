package enums;
import people.Human;

public enum Emotion {
    HAPPY("Веселый", "Веселая"),
    SAD("Грустный", "Грустная"),
    RESENTMENT("Обиделся", "Обиделась"),
    DEFAULT("Нормальный", "Нормальная");

    private String maleText;
    private String femaleText;

    Emotion(String text){
        this.maleText = text;
        this.femaleText = text;
    }
    Emotion(String maleText, String femaleText){
        this.maleText = maleText;
        this.femaleText = femaleText;
    }
    public String getDescription(Human person) {
        return person.isMale() ? maleText : femaleText;
    }

}
