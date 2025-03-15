package utils.responses;

public class AnswerString<T extends String> extends Answer {
    private T answer;


    public AnswerString(T answer) {
        super(answer);
    }

    public T getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return answer;
    }
}
