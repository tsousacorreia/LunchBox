package br.com.etecia.lunchbox;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConstrutoresFragment extends Fragment implements OnAlimentoClickListener {

    private RecyclerView recyclerView;
    private AlimentosAdapter adapter;
    private OnAlimentoSelectedListener listener;
    private PerfilViewModel perfilViewModel;
    private SharedViewModel sharedViewModel;
    private Button btnVisualizarLancheira;  // Botão para navegar para a Lancheira

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

        // Inicializa o PerfilViewModel e SharedViewModel
        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Inicializa o botão "Visualizar Lancheira"
        btnVisualizarLancheira = view.findViewById(R.id.btn_visualizar_lancheira);
        btnVisualizarLancheira.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVisualizarLancheira();  // Aciona a navegação para o fragmento da Lancheira
            }
        });

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
        if (perfilViewModel.getPerfilSelecionado().getValue() != null) {
            sharedViewModel.adicionarAlimento(alimento);
            Toast.makeText(getContext(), alimento.getNome() + " adicionado à lancheira!", Toast.LENGTH_SHORT).show();
        } else {
            showError("Por favor, selecione um perfil antes de escolher alimentos.");
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
        listener = null;
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}