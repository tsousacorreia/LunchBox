package br.com.etecia.lunchbox;

import java.util.List;

public class Lancheira {
    private int id;
    private String nomeLancheira;
    private String diaSemana;
    private List<Alimentos> alimentos;

    public Lancheira(int id, String nomeLancheira, String diaSemana, List<Alimentos> alimentos) {
        this.id = id;
        this.nomeLancheira = nomeLancheira;
        this.diaSemana = diaSemana;
        this.alimentos = alimentos;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeLancheira() {
        return nomeLancheira;
    }

    public void setNomeLancheira(String nomeLancheira) {
        this.nomeLancheira = nomeLancheira;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public List<Alimentos> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<Alimentos> alimentos) {
        this.alimentos = alimentos;
    }
}