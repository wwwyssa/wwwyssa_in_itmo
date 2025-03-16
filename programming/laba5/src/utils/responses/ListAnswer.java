package utils.responses;
import models.Product;

import java.util.ArrayList;


public class ListAnswer implements ValidAnswer {
    private final ArrayList<Object> value;

    public ListAnswer(ArrayList<Object> value) {
        this.value = value;
    }

    public ArrayList<Object> getValue() {
        return value;
    }

    @Override
    public Object getAnswer() {
        return value;
    }
}