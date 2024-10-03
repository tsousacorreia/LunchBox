package br.com.etecia.lunchbox;

import java.util.List;

public class SnacksResponse {
    private boolean error;
    private String message;
    private List<Alimentos> snacks;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<Alimentos> getSnacks() {
        return snacks;
    }
}