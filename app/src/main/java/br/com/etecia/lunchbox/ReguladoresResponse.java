package br.com.etecia.lunchbox;

import java.util.List;

public class ReguladoresResponse {
    private boolean error;
    private String message;
    private List<FoodItem> reguladores;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<FoodItem> getReguladores() {
        return reguladores;
    }
}