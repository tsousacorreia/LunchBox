package br.com.etecia.lunchbox;

public class Perfil {
    private int id;
    private String nome;
    private int idade;
    private String preferencias;

    public Perfil(int id, String nome, int idade, String preferencias) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.preferencias = preferencias;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getPreferencias() {
        return preferencias;
    }
}