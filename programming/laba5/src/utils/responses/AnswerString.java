package utils.responses;

public class AnswerString implements ValidAnswer<String> {
    private final String value;

    public AnswerString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getAnswer() {
        return value;
    }
}