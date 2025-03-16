package utils.responses;
import java.util.ArrayList;


public class ListAnswer implements ValidAnswer {
    private final ArrayList<String> value;

    public ListAnswer(ArrayList<String> value) {
        this.value = value;
    }

    public ArrayList<String> getValue() {
        return value;
    }

    @Override
    public Object getAnswer() {
        return value;
    }
}