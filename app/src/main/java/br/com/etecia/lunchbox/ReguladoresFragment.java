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

public class ReguladoresFragment extends Fragment implements OnAlimentoClickListener {

    private RecyclerView recyclerView;
    private AlimentosAdapter adapter;
    private OnAlimentoSelectedListener listener;
    private PerfilViewModel perfilViewModel;
    private SharedViewModel sharedViewModel;
    private Button btnVisualizarLancheira;  // Botão para navegar para a Lancheira

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reguladores, container, false);

        // Inicializa o RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_reguladores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa o PerfilViewModel e SharedViewModel
        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Configura o adapter passando o SharedViewModel, PerfilViewModel e o listener para cliques nos alimentos
        adapter = new AlimentosAdapter(sharedViewModel, perfilViewModel, this);
        recyclerView.setAdapter(adapter);

        // Inicializa o botão "Visualizar Lancheira"
        btnVisualizarLancheira = view.findViewById(R.id.btn_visualizar_lancheira);
        btnVisualizarLancheira.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVisualizarLancheira();  // Aciona a navegação para o fragmento da Lancheira
            }
        });

        // Carrega os alimentos da API
        loadReguladores();
        return view;
    }

    private void loadReguladores() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<ReguladoresResponse> call = apiService.getReguladores();
        call.enqueue(new Callback<ReguladoresResponse>() {
            @Override
            public void onResponse(Call<ReguladoresResponse> call, Response<ReguladoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReguladoresResponse apiResponse = response.body();
                    if (apiResponse.isError()) {
                        showError(apiResponse.getMessage());
                    } else {
                        adapter.setAlimentos(apiResponse.getReguladores());
                    }
                } else {
                    showError("Resposta inválida da API");
                }
            }

            @Override
            public void onFailure(Call<ReguladoresResponse> call, Throwable t) {
                showError("Falha na comunicação com a API: " + t.getMessage());
            }
        });
    }

    @Override
    public void onAlimentoClick(Alimentos alimento) {
        if (listener != null) {
            listener.onAlimentoSelected(alimento);
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}