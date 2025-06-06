package com.wwwyssa.lab7.common.util.executions;

import com.wwwyssa.lab7.common.util.ValidAnswer;

public class Answer<T extends ValidAnswer> {
    private final T value;

    public Answer(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}