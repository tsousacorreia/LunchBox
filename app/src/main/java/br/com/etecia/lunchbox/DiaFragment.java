package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiaFragment extends Fragment {

    private static final String ARG_DATE_LABEL = "dateLabel";
    private RecyclerView recyclerViewLancheira;
    private DiaLancheiraAdapter diaLancheiraAdapter;

    public static DiaFragment newInstance(int lancheiraId, String dateLabel) {
        DiaFragment fragment = new DiaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE_LABEL, dateLabel);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dia, container, false);

        // Configura o RecyclerView pai
        recyclerViewLancheira = view.findViewById(R.id.recyclerViewLancheira);
        recyclerViewLancheira.setLayoutManager(new LinearLayoutManager(getContext()));

        // Carrega e exibe as lancheiras
        carregarLancheiras();

        return view;
    }

    private void carregarLancheiras() {
        SQLiteHelper dbHelper = SQLiteHelper.getInstance(getContext());

        // Obtém a data do argumento passado para o fragmento
        String dateLabel = getArguments().getString(ARG_DATE_LABEL);

        // Verifique se a data foi passada corretamente
        if (dateLabel != null) {
            // Carrega as lancheiras para a data especificada
            List<Lancheira> lancheiras = dbHelper.obterLancheirasPorData(dateLabel);

            // Configura o adapter do RecyclerView com as lancheiras
            diaLancheiraAdapter = new DiaLancheiraAdapter(getContext(), lancheiras, dateLabel);
            recyclerViewLancheira.setAdapter(diaLancheiraAdapter);
        }
    }
}