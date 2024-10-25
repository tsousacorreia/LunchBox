package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DiasFragment extends Fragment {

    private static final String ARG_DAY = "day";
    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private TextView textDataLancheira;
    private RecyclerView recyclerViewLancheira;
    private LancheiraAdapter adapter;
    private SQLiteHelper databaseHelper;
    private String dataFormatada;

    public static DiasFragment newInstance(int day, int year, int month) {
        DiasFragment fragment = new DiasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY, day);
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textDataLancheira = view.findViewById(R.id.text_data_lancheira);
        recyclerViewLancheira = view.findViewById(R.id.recycler_view_lancheira_dia);
        recyclerViewLancheira.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LancheiraAdapter(new ArrayList<>());
        recyclerViewLancheira.setAdapter(adapter);

        databaseHelper = new SQLiteHelper(getContext());

        if (getArguments() != null) {
            int day = getArguments().getInt(ARG_DAY);
            int year = getArguments().getInt(ARG_YEAR);
            int month = getArguments().getInt(ARG_MONTH);

            // Formata a data
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dataFormatada = sdf.format(calendar.getTime());

            // Exibe a data formatada
            textDataLancheira.setText("Lancheira de " + dataFormatada);

            // Carrega a lancheira do banco de dados para a data
            carregarLancheira();
        }
    }

    private void carregarLancheira() {
        List<Alimentos> lancheira = databaseHelper.buscarLancheiraPorData(dataFormatada);
        if (lancheira != null && !lancheira.isEmpty()) {
            adapter.setAlimentos(lancheira);
        } else {
            textDataLancheira.setText("Nenhuma lancheira salva para " + dataFormatada);
        }
    }
}