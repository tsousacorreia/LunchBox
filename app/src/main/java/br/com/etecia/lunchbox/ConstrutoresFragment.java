package br.com.etecia.lunchbox;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConstrutoresFragment extends Fragment implements OnAlimentoClickListener {

    private RecyclerView recyclerView;
    private AlimentosAdapter adapter;
    private OnAlimentoSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_construtores, container, false);

        // Inicializa o RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_construtores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configura o adapter e o listener para cliques nos alimentos
        adapter = new AlimentosAdapter(this);
        recyclerView.setAdapter(adapter);

        // Carrega os alimentos da API
        loadConstrutores();
        return view;
    }

    private void loadConstrutores() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<ConstrutoresResponse> call = apiService.getConstrutores();
        call.enqueue(new Callback<ConstrutoresResponse>() {
            @Override
            public void onResponse(Call<ConstrutoresResponse> call, Response<ConstrutoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConstrutoresResponse apiResponse = response.body();
                    if (apiResponse.isError()) {
                        showError(apiResponse.getMessage());
                    } else {
                        adapter.setAlimentos(apiResponse.getConstrutores());
                    }
                } else {
                    showError("Resposta inválida da API");
                }
            }

            @Override
            public void onFailure(Call<ConstrutoresResponse> call, Throwable t) {
                showError("Falha na comunicação com a API: " + t.getMessage());
            }
        });
    }

    @Override
    public void onAlimentoClick(Alimentos alimento) {
        // Notifica a atividade ou fragmento responsável pela lancheira que um alimento foi selecionado
        if (listener != null) {
            listener.onAlimentoSelected(alimento);
        } else {
            showError("Listener de seleção de alimentos não está configurado.");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAlimentoSelectedListener) {
            listener = (OnAlimentoSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " deve implementar OnAlimentoSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null; // Evita leaks de memória
    }

    private void showError(String message) {
        // Verifica se o contexto está disponível antes de tentar exibir o Toast
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}