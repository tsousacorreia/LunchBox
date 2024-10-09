package br.com.etecia.lunchbox;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PerfilViewModel extends ViewModel {

    private final MutableLiveData<Perfil> perfilSelecionado = new MutableLiveData<>();

    public void setPerfilSelecionado(Perfil perfil) {
        perfilSelecionado.setValue(perfil);
    }

    public LiveData<Perfil> getPerfilSelecionado() {
        return perfilSelecionado;
    }
}
