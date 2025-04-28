package com.wwwyssa.lab6.common.util;

import com.wwwyssa.lab6.common.models.Product;
import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 11L;
    private String string;
    private Product product = null;

    public Request(String string) {
        this.string = string;
    }

    public Request(String string, Product product) {
        this.string = string;
        this.product = product;
    }

    public String[] getCommand() {
        String[] inputCommand = (string.trim() + " ").split(" ", 2);
        inputCommand[1] = inputCommand[1].trim();
        return inputCommand;
    }

    public Product getProduct() {
        return product;
    }

    public void setCommand(String command) {
        this.string = command;
    }

    public void setBand(Product band) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Request{" +
                "string='" + string + '\'' +
                ", product=" + product +
                '}';
    }
}