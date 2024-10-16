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

public class SnacksFragment extends Fragment implements OnAlimentoClickListener {

    private RecyclerView recyclerView;
    private AlimentosAdapter adapter;
    private OnAlimentoSelectedListener listener;
    private PerfilViewModel perfilViewModel;
    private SharedViewModel sharedViewModel;
    private Button btnVisualizarLancheira;  // Botão para navegar para a Lancheira

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        // Inicializa o RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_snacks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa o PerfilViewModel e SharedViewModel
        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Configura o adapter passando o SharedViewModel e o listener para cliques nos alimentos
        adapter = new AlimentosAdapter(sharedViewModel, this);
        recyclerView.setAdapter(adapter);

        // Inicializa o botão "Visualizar Lancheira"
        btnVisualizarLancheira = view.findViewById(R.id.btn_visualizar_lancheira);
        btnVisualizarLancheira.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVisualizarLancheira();  // Aciona a navegação para o fragmento da Lancheira
            }
        });

        // Carrega os alimentos da API
        loadSnacks();
        return view;
    }

    private void loadSnacks() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<SnacksResponse> call = apiService.getSnacks();
        call.enqueue(new Callback<SnacksResponse>() {
            @Override
            public void onResponse(Call<SnacksResponse> call, Response<SnacksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SnacksResponse apiResponse = response.body();
                    if (apiResponse.isError()) {
                        showError(apiResponse.getMessage());
                    } else {
                        adapter.setAlimentos(apiResponse.getSnacks());
                    }
                } else {
                    showError("Resposta inválida da API");
                }
            }

            @Override
            public void onFailure(Call<SnacksResponse> call, Throwable t) {
                showError("Falha na comunicação com a API: " + t.getMessage());
            }
        });
    }

    @Override
    public void onAlimentoClick(Alimentos alimento) {
        // Verifica se um perfil está selecionado
        if (perfilViewModel.getPerfilSelecionado().getValue() != null) {
            // Verifica se o alimento já foi adicionado à lancheira
            if (sharedViewModel.isAlimentoAdicionado(alimento)) {
                // Mostra um alerta ao usuário informando que o alimento já foi adicionado
                Toast.makeText(getContext(), alimento.getNome() + " já foi adicionado à lancheira!", Toast.LENGTH_SHORT).show();
            } else {
                // Adiciona o alimento ao SharedViewModel para que seja acessível no LancheiraFragment
                sharedViewModel.adicionarAlimento(alimento);

                // Mostra um Toast confirmando a adição do alimento
                Toast.makeText(getContext(), alimento.getNome() + " adicionado à lancheira!", Toast.LENGTH_SHORT).show();

                // Notifica o fragmento responsável pela lancheira que um alimento foi selecionado (se o listener estiver configurado)
                if (listener != null) {
                    listener.onAlimentoSelected(alimento);
                }
            }
        } else {
            // Mostra uma mensagem de erro se nenhum perfil estiver selecionado
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