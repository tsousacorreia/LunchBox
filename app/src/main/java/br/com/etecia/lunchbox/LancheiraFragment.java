package br.com.etecia.lunchbox;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LancheiraFragment extends Fragment implements OnAlimentoSelectedListener {

    private TextView textData;
    private RecyclerView recyclerView;
    private LancheiraAdapter adapter;
    private List<Alimentos> alimentosNaLancheira;
    private Button btnFinalizarLancheira;
    private Button btnLimparLancheira;
    private SQLiteHelper databaseHelper;
    private PerfilViewModel perfilViewModel;
    private String dataSelecionada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lancheira, container, false);

        // Inicializa o TextView para seleção de data
        textData = view.findViewById(R.id.text_data);
        textData.setOnClickListener(v -> showDatePickerDialog());

        // Configuração do RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_lancheira);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Inicialização da lista de alimentos e do adaptador
        alimentosNaLancheira = new ArrayList<>();
        adapter = new LancheiraAdapter(alimentosNaLancheira);
        recyclerView.setAdapter(adapter);

        // Configuração do ItemTouchHelper para remoção de alimentos
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

        // Configuração dos botões
        btnFinalizarLancheira = view.findViewById(R.id.btn_finalizar_lancheira);
        btnFinalizarLancheira.setOnClickListener(v -> finalizarLancheira());

        btnLimparLancheira = view.findViewById(R.id.btn_limpar_lancheira);
        btnLimparLancheira.setOnClickListener(v -> limparLancheira());

        // Inicializa o ViewModel do perfil
        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

        // Inicializa o helper de banco de dados
        databaseHelper = new SQLiteHelper(getContext());

        return view;
    }

    private void finalizarLancheira() {
        if (alimentosNaLancheira.isEmpty()) {
            Toast.makeText(getContext(), "Adicione alimentos à lancheira antes de finalizar", Toast.LENGTH_SHORT).show();
        } else if (dataSelecionada == null) {
            Toast.makeText(getContext(), "Selecione uma data antes de finalizar", Toast.LENGTH_SHORT).show();
        } else {
            // Observa o perfil selecionado
            perfilViewModel.getPerfilSelecionado().observe(getViewLifecycleOwner(), perfil -> {
                if (perfil != null) {
                    // Salva a lancheira no banco de dados local com a data selecionada
                    long lancheiraId = salvarLancheira(perfil);
                    if (lancheiraId != -1) {
                        Toast.makeText(getContext(), "Lancheira finalizada e salva!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Erro ao salvar a lancheira", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Selecione um perfil antes de finalizar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private long salvarLancheira(Perfil perfil) {
        // Salva a lancheira e retorna o ID da lancheira com a data selecionada
        long lancheiraId = databaseHelper.inserirLancheira(perfil.getId(), alimentosNaLancheira, dataSelecionada);
        return lancheiraId;
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

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    dataSelecionada = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    textData.setText(dataSelecionada);
                },
                year, month, day
        );

        datePickerDialog.show();
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