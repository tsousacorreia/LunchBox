package br.com.etecia.lunchbox.lunchbox;

import java.util.List;

public class ReguladoresResponse {
    private boolean error;
    private String message;
    private List<Alimentos> reguladores;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<Alimentos> getReguladores() {
        return reguladores;
    }
}