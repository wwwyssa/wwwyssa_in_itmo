package utils.responses;
import models.Product;

import java.util.List;


public class ListAnswer implements ValidAnswer<List<Product>> {
    private final List<Product> value;

    public ListAnswer(List<Product> value) {
        this.value = value;
    }

    public List<Product> getValue() {
        return value;
    }

    @Override
    public List<Product> getAnswer() {
        return value;
    }
}