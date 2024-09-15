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
    private Button btnLimparLancheira;  // Botão para limpar lancheira

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lancheira, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_lancheira);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adicionando o divisor entre os itens do RecyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Inicializar a lista de alimentos na lancheira
        alimentosNaLancheira = new ArrayList<>();

        // Configurar o adaptador da lancheira
        adapter = new LancheiraAdapter(alimentosNaLancheira);
        recyclerView.setAdapter(adapter);

        // Adicionar swipe para remover itens
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;  // Não permitimos mover itens
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Remover o item da posição
                int position = viewHolder.getAdapterPosition();
                removerAlimentoDaLancheira(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Configurar o botão de finalizar lancheira
        btnFinalizarLancheira = view.findViewById(R.id.btn_finalizar_lancheira);
        btnFinalizarLancheira.setOnClickListener(v -> finalizarLancheira());

        // Configurar o botão de limpar lancheira
        btnLimparLancheira = view.findViewById(R.id.btn_limpar_lancheira);
        btnLimparLancheira.setOnClickListener(v -> limparLancheira());

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

    // Método para limpar todos os alimentos da lancheira
    private void limparLancheira() {
        if (!alimentosNaLancheira.isEmpty()) {
            alimentosNaLancheira.clear();  // Limpa a lista de alimentos
            adapter.notifyDataSetChanged();  // Notifica o adaptador da mudança
            Toast.makeText(getContext(), "Lancheira limpa!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "A lancheira já está vazia", Toast.LENGTH_SHORT).show();
        }
    }

    // Implementação do método da interface OnAlimentoSelectedListener
    @Override
    public void onAlimentoSelected(Alimentos alimento) {
        adicionarAlimentoALancheira(alimento);  // Adiciona o alimento à lancheira
    }

    // Método para adicionar alimentos à lancheira
    public void adicionarAlimentoALancheira(Alimentos alimento) {
        alimentosNaLancheira.add(alimento);
        adapter.notifyDataSetChanged();
    }

    // Método para remover alimentos da lancheira
    public void removerAlimentoDaLancheira(int position) {
        if (position >= 0 && position < alimentosNaLancheira.size()) {
            alimentosNaLancheira.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Alimento removido da lancheira", Toast.LENGTH_SHORT).show();
        }
    }
}