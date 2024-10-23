package br.com.etecia.lunchbox;

import java.util.Objects;

public class Alimentos {
    private int id;
    private String nome;
    private String descricao;
    private String imagemUrl;

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    // Implementação dos métodos equals() e hashCode() com base no nome
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alimentos alimentos = (Alimentos) o;
        return Objects.equals(nome, alimentos.nome); // Dois alimentos são considerados iguais se o nome for o mesmo
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome); // O hashCode é baseado no nome
    }
}