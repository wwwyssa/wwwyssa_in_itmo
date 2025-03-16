package utils.responses;

public class Answer<T extends ValidAnswer> {
    private final T value;

    public Answer(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}