package utils.responses;

public class AnswerString implements ValidAnswer {
    private final String value;

    public AnswerString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object getAnswer() {
        return value;
    }
}