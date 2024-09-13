package br.com.etecia.lunchbox;

import java.util.List;

public class ConstrutoresResponse {
    private boolean error;
    private String message;
    private List<FoodItem> construtores;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<FoodItem> getConstrutores() {
        return construtores;
    }
}