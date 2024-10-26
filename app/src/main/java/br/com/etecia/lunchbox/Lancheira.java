package br.com.etecia.lunchbox;

import java.util.List;

public class Lancheira {
    private int id;
    private String nomeLancheira;
    private String data;
    private List<Alimentos> alimentos;
    private int perfilId;

    // Construtor atualizado
    public Lancheira(int id, String nomeLancheira, String data, List<Alimentos> alimentos, int perfilId) {
        this.id = id;
        this.nomeLancheira = nomeLancheira;
        this.data = data;
        this.alimentos = alimentos;
        this.perfilId = perfilId;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<Alimentos> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<Alimentos> alimentos) {
        this.alimentos = alimentos;
    }

    public int getPerfilId() { // Adicionado getter para perfilId
        return perfilId;
    }

    public void setPerfilId(int perfilId) { // Adicionado setter para perfilId
        this.perfilId = perfilId;
    }
}