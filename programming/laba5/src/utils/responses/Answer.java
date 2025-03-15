package utils.responses;

public class Answer<T> {
    private T answer;

    public Answer(T answer) {
        this.answer = answer;
    }

    public T getAnswer() {
        return answer;
    }

}
