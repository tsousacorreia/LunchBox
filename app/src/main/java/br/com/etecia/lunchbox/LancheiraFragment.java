package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LancheiraFragment extends Fragment implements OnAlimentoSelectedListener {

    private RecyclerView recyclerView;
    private LancheiraAdapter adapter;
    private List<FoodItem> alimentosNaLancheira;
    private Button btnFinalizarLancheira;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lancheira, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_lancheira);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar a lista de alimentos na lancheira
        alimentosNaLancheira = new ArrayList<>();

        // Configurar o adaptador da lancheira
        adapter = new LancheiraAdapter(alimentosNaLancheira);
        recyclerView.setAdapter(adapter);

        // Configurar o botão de finalizar lancheira
        btnFinalizarLancheira = view.findViewById(R.id.btn_finalizar_lancheira);
        btnFinalizarLancheira.setOnClickListener(v -> finalizarLancheira());

        return view;
    }

    private void finalizarLancheira() {
        if (alimentosNaLancheira.isEmpty()) {
            Toast.makeText(getContext(), "Adicione alimentos à lancheira antes de finalizar", Toast.LENGTH_SHORT).show();
        } else {
            // Lógica para finalizar a lancheira, ex: salvar no banco ou exibir resumo
            Toast.makeText(getContext(), "Lancheira finalizada!", Toast.LENGTH_SHORT).show();
        }
    }

    // Implementação do método da interface OnAlimentoSelectedListener
    @Override
    public void onAlimentoSelected(FoodItem alimento) {
        adicionarAlimentoALancheira(alimento);  // Adiciona o alimento à lancheira
    }

    // Método para adicionar alimentos à lancheira
    public void adicionarAlimentoALancheira(FoodItem alimento) {
        alimentosNaLancheira.add(alimento);
        adapter.notifyDataSetChanged();
    }
}