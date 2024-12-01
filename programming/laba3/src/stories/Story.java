package stories;

import people.Human;

import java.util.Objects;

public class Story {
    public Story(){};
    final private String storyName = "Базовая (пустая) история";
    public void start(){
        System.out.println("Начало");
    }

    @Override
    public String toString() {
        return "Story {" + "storyName='" + storyName + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Story story = (Story) o;
        return Objects.equals(storyName, story.storyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storyName);
    }
}
