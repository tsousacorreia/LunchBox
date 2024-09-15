package br.com.etecia.lunchbox;

import java.util.List;

public class PerfisResponse {
    private boolean error;
    private String message;
    private List<Perfil> perfis;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public static class Perfil {
        private int id;
        private String nome;
        private int idade;
        private String alergias;
        private String preferencias;

        public int getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public int getIdade() {
            return idade;
        }

        public String getAlergias() {
            return alergias;
        }

        public String getPreferencias() {
            return preferencias;
        }
    }
}