package utils.responses;

import models.Product;

import java.util.ArrayList;

public class AnswerWithElements extends Answer<ArrayList<Product>>{
    private ArrayList<Product> answer;

    public AnswerWithElements(T answer) {
        super(answer);
    }

    public T getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return answer.toString();
    }
}
