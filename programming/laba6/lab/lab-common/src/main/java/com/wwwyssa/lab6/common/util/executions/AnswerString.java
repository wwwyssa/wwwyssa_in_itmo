package com.wwwyssa.lab6.common.util.executions;

import com.wwwyssa.lab6.common.util.ValidAnswer;
import java.io.Serial;
import java.io.Serializable;

public class AnswerString implements ValidAnswer<String>, Serializable {

    @Serial
    private static final long serialVersionUID = 14L;

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

    @Override
    public String toString() {
        return value;
    }
}