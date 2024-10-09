package br.com.etecia.lunchbox;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Alimentos>> alimentosSelecionados = new MutableLiveData<>(new ArrayList<>());

    // Método para adicionar um alimento à lista de selecionados
    public void adicionarAlimento(Alimentos alimento) {
        List<Alimentos> currentList = alimentosSelecionados.getValue();
        if (currentList != null) {
            currentList.add(alimento);
            alimentosSelecionados.setValue(currentList);
        }
    }

    // Método para observar a lista de alimentos selecionados
    public LiveData<List<Alimentos>> getAlimentosSelecionados() {
        return alimentosSelecionados;
    }

    // Método para limpar a lista de alimentos
    public void limparAlimentos() {
        List<Alimentos> currentList = alimentosSelecionados.getValue();
        if (currentList != null) {
            currentList.clear();
            alimentosSelecionados.setValue(currentList);
        }
    }

    // Método para remover um alimento específico
    public void limparAlimentos(Alimentos alimento) {
        List<Alimentos> currentList = alimentosSelecionados.getValue();
        if (currentList != null && currentList.contains(alimento)) {
            currentList.remove(alimento);
            alimentosSelecionados.setValue(currentList);
        }
    }
}