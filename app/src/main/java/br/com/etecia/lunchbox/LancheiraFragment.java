package br.com.etecia.lunchbox;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;  // Adicione esta linha
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

public class LancheiraFragment extends Fragment {

    private TextView textData;
    private RecyclerView recyclerView;
    private LancheiraAdapter adapter;
    private Button btnFinalizarLancheira;
    private Button btnLimparLancheira;
    private SQLiteHelper databaseHelper;
    private PerfilViewModel perfilViewModel;
    private SharedViewModel sharedViewModel;
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
        adapter = new LancheiraAdapter(new ArrayList<>());
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

        // Inicializa o ViewModel do perfil para compartilhamento de dados entre fragments
        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observa os alimentos adicionados no SharedViewModel
        sharedViewModel.getAlimentosSelecionados().observe(getViewLifecycleOwner(), alimentos -> {
            adapter.setAlimentos(alimentos);
        });

        // Inicializa o helper de banco de dados
        databaseHelper = new SQLiteHelper(requireContext());  // Usando requireContext()

        return view;
    }

    private void finalizarLancheira() {
        List<Alimentos> alimentosNaLancheira = adapter.getAlimentos();
        if (alimentosNaLancheira.isEmpty()) {
            Toast.makeText(getContext(), "Adicione alimentos à lancheira antes de finalizar", Toast.LENGTH_SHORT).show();
        } else if (dataSelecionada == null) {
            Toast.makeText(getContext(), "Selecione uma data antes de finalizar", Toast.LENGTH_SHORT).show();
        } else {
            perfilViewModel.getPerfilSelecionado().removeObservers(getViewLifecycleOwner()); // Remove observadores antigos
            perfilViewModel.getPerfilSelecionado().observe(getViewLifecycleOwner(), perfil -> {
                if (perfil != null) {
                    long lancheiraId = salvarLancheira(perfil, alimentosNaLancheira);
                    if (lancheiraId != -1) {
                        Toast.makeText(getContext(), "Lancheira finalizada e salva!", Toast.LENGTH_SHORT).show();
                        sharedViewModel.limparAlimentos();
                        dataSelecionada = null;
                        textData.setText("Selecione uma data");
                    } else {
                        Log.e("LancheiraFragment", "Erro ao salvar a lancheira: ID retornado -1"); // Log do erro
                        Toast.makeText(getContext(), "Erro ao salvar a lancheira", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Selecione um perfil antes de finalizar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private long salvarLancheira(Perfil perfil, List<Alimentos> alimentosNaLancheira) {
        return databaseHelper.inserirLancheira(perfil.getId(), alimentosNaLancheira, dataSelecionada);
    }

    private void limparLancheira() {
        sharedViewModel.limparAlimentos();
        Toast.makeText(getContext(), "Lancheira limpa!", Toast.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    dataSelecionada = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textData.setText(dataSelecionada);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void removerAlimentoDaLancheira(int position) {
        List<Alimentos> alimentosNaLancheira = adapter.getAlimentos();
        Alimentos alimentoRemovido = alimentosNaLancheira.get(position);
        sharedViewModel.limparAlimento(alimentoRemovido);
        Toast.makeText(getContext(), alimentoRemovido.getNome() + " removido da lancheira", Toast.LENGTH_SHORT).show();
    }
}