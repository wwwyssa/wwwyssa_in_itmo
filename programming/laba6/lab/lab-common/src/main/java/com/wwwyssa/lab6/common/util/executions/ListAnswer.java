package com.wwwyssa.lab6.common.util.executions;
import com.wwwyssa.lab6.common.util.ValidAnswer;
import com.wwwyssa.lab6.common.models.Product;

import java.util.List;


public class ListAnswer implements ValidAnswer<List<?>> {
    private final List<?> value;

    public ListAnswer(List<?> value) {
        this.value = value;
    }

    @Override
    public List<?> getAnswer() {
        return value;
    }
}