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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LancheiraFragment extends Fragment implements OnAlimentoSelectedListener {

    private RecyclerView recyclerView;
    private LancheiraAdapter adapter;
    private List<Alimentos> alimentosNaLancheira;
    private Button btnFinalizarLancheira;
    private Button btnLimparLancheira;
    private String nomeLancheira;
    private String dataLancheira;
    private int perfilId;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lancheira, container, false);

        if (getArguments() != null) {
            nomeLancheira = getArguments().getString("nome_lancheira");
            dataLancheira = getArguments().getString("data_lancheira");
            perfilId = getArguments().getInt("perfil_id");
        }

        recyclerView = view.findViewById(R.id.recycler_view_lancheira);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        alimentosNaLancheira = new ArrayList<>();
        adapter = new LancheiraAdapter(alimentosNaLancheira);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removerAlimentoDaLancheira(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        btnFinalizarLancheira = view.findViewById(R.id.btn_finalizar_lancheira);
        btnFinalizarLancheira.setOnClickListener(v -> finalizarLancheira());

        btnLimparLancheira = view.findViewById(R.id.btn_limpar_lancheira);
        btnLimparLancheira.setOnClickListener(v -> limparLancheira());

        return view;
    }

    private void finalizarLancheira() {
        if (alimentosNaLancheira.isEmpty()) {
            Toast.makeText(getContext(), "Adicione alimentos à lancheira antes de finalizar", Toast.LENGTH_SHORT).show();
        } else {
            // Salvar a lancheira no banco de dados local
            long lancheiraId = salvarLancheira();
            if (lancheiraId != -1) {
                // Salvar alimentos da lancheira
                for (Alimentos alimento : alimentosNaLancheira) {
                    salvarAlimentoNaLancheira(lancheiraId, alimento.getId());
                }
                Toast.makeText(getContext(), "Lancheira finalizada e salva!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Erro ao salvar a lancheira", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private long salvarLancheira() {
        return databaseHelper.addLancheira(nomeLancheira, dataLancheira, perfilId);
    }

    private void salvarAlimentoNaLancheira(long lancheiraId, int alimentoId) {
        databaseHelper.addAlimentoNaLancheira(lancheiraId, alimentoId);
    }

    private void limparLancheira() {
        if (!alimentosNaLancheira.isEmpty()) {
            alimentosNaLancheira.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Lancheira limpa!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "A lancheira já está vazia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAlimentoSelected(Alimentos alimento) {
        adicionarAlimentoALancheira(alimento);
    }

    public void adicionarAlimentoALancheira(Alimentos alimento) {
        alimentosNaLancheira.add(alimento);
        adapter.notifyDataSetChanged();
    }

    public void removerAlimentoDaLancheira(int position) {
        if (position >= 0 && position < alimentosNaLancheira.size()) {
            alimentosNaLancheira.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Alimento removido da lancheira", Toast.LENGTH_SHORT).show();
        }
    }
}