package br.com.etecia.lunchbox;

public class Perfil {
    private int id;
    private String nome;
    private int idade;
    private String preferencias;

    // Construtor modificado
    public Perfil(int id, String nome, int idade, String preferencias) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.preferencias = preferencias;
    }

    // Getters e setters (se necessário)
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

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }
}