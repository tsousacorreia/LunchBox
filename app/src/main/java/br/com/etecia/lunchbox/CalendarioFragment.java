package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CalendarioFragment extends Fragment {

    private RecyclerView recyclerView;
    private LancheiraProntaAdapter adapter;
    private SQLiteHelper databaseHelper;
    private PerfilViewModel perfilViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        // Inicializa o RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_calendario);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LancheiraProntaAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Inicializa o SQLiteHelper
        databaseHelper = new SQLiteHelper(requireContext());

        // Inicializa o ViewModel
        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

        // Obter lancheiras salvas do banco de dados
        carregarLancheiras();

        return view;
    }

    private void carregarLancheiras() {
        perfilViewModel.getPerfilSelecionado().observe(getViewLifecycleOwner(), new Observer<Perfil>() {
            @Override
            public void onChanged(Perfil perfil) {
                if (perfil != null) {
                    List<Lancheira> lancheiras = databaseHelper.obterLancheirasPorPerfil(perfil.getId());
                    if (lancheiras.isEmpty()) {
                        Toast.makeText(getContext(), "Nenhuma lancheira encontrada para este perfil", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.setLancheiras(lancheiras);
                    }
                }
            }
        });
    }
}