package br.com.etecia.lunchbox;

public class Perfil {
    private int id;
    private String nome;
    private int idade;
    private String preferencias;

    // Construtor que aceita nome, idade e preferências
    public Perfil(String nome, int idade, String preferencias) {
        this.nome = nome;
        this.idade = idade;
        this.preferencias = preferencias;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public int getIdade() {
        return idade;
    }
}