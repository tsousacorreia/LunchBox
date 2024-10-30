package br.com.etecia.lunchbox;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Alimentos>> alimentosSelecionados = new MutableLiveData<>(new ArrayList<>());

    // Método para adicionar alimento à lancheira
    public void adicionarAlimento(Alimentos alimento) {
        List<Alimentos> listaAtual = new ArrayList<>(alimentosSelecionados.getValue());

        if (isAlimentoAdicionado(alimento)) {
            Log.d("SharedViewModel", "Alimento já está na lista: " + alimento.getNome());
            return;
        }

        listaAtual.add(alimento);
        alimentosSelecionados.setValue(listaAtual);
        Log.d("SharedViewModel", "Alimento adicionado: " + alimento.getNome());
    }


    // Verifica se o alimento já foi adicionado
    public boolean isAlimentoAdicionado(Alimentos alimento) {
        List<Alimentos> listaAtual = alimentosSelecionados.getValue();
        return listaAtual != null && listaAtual.contains(alimento);
    }

    // Método para obter a lista de alimentos selecionados
    public LiveData<List<Alimentos>> getAlimentosSelecionados() {
        return alimentosSelecionados;
    }

    // Método para limpar a lista de alimentos
    public void limparAlimentos() {
        alimentosSelecionados.setValue(new ArrayList<>());
    }

    // Método para remover um alimento específico
    public void limparAlimento(Alimentos alimento) {
        List<Alimentos> currentList = new ArrayList<>(alimentosSelecionados.getValue());
        if (currentList.contains(alimento)) {
            currentList.remove(alimento);
            alimentosSelecionados.setValue(currentList);
        }
    }
}