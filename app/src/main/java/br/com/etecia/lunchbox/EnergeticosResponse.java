package br.com.etecia.lunchbox;

import java.util.List;

public class EnergeticosResponse {
    private boolean error;
    private String message;
    private List<Alimentos> energeticos;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<Alimentos> getEnergeticos() {
        return energeticos;
    }
}