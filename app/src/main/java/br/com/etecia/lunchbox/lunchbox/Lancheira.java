package br.com.etecia.lunchbox.lunchbox;

import java.util.List;
import java.util.stream.Collectors;

public class Lancheira {
    private int id;
    private String nomeLancheira;
    private String data;
    private List<Alimentos> alimentos;
    private Perfil perfil;  // Armazenando o objeto Perfil diretamente

    // Construtor atualizado
    public Lancheira(int id, String nomeLancheira, String data, List<Alimentos> alimentos, Perfil perfil) {
        this.id = id;
        this.nomeLancheira = nomeLancheira != null ? nomeLancheira : "";
        this.data = data;
        this.alimentos = alimentos;
        this.perfil = perfil;  // Agora você passa o objeto Perfil
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
        if (nomeLancheira != null && !nomeLancheira.trim().isEmpty()) {
            this.nomeLancheira = nomeLancheira;
        } else {
            throw new IllegalArgumentException("Nome da lancheira não pode ser vazio.");
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data; // Considerar validação da data
    }

    public List<Alimentos> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<Alimentos> alimentos) {
        this.alimentos = alimentos;
    }

    public Perfil getPerfil() {  // Método para acessar o Perfil diretamente
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    // Método para obter nomes dos alimentos
    public List<String> getNomesAlimentos() {
        return alimentos.stream()
                .map(Alimentos::getNome)
                .collect(Collectors.toList());
    }

    // Método para obter descrições dos alimentos
    public String getAlimentosDescription() {
        StringBuilder descricao = new StringBuilder();
        for (Alimentos alimento : alimentos) {
            descricao.append(alimento.getNome()).append(", ");
        }
        if (descricao.length() > 0) {
            descricao.setLength(descricao.length() - 2);
        }
        return descricao.toString();
    }

    // Método toString
    @Override
    public String toString() {
        return "Lancheira{" +
                "id=" + id +
                ", nomeLancheira='" + nomeLancheira + '\'' +
                ", data='" + data + '\'' +
                ", perfil=" + perfil.getNome() +
                ", alimentos=" + getNomesAlimentos() +
                '}';
    }
}