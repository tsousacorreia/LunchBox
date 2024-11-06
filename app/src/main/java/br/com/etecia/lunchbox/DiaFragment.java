package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiaFragment extends Fragment {

    private static final String ARG_LANCHEIRA_ID = "lancheiraId";
    private static final String ARG_DATE_LABEL = "dateLabel";
    private int lancheiraId;

    public static DiaFragment newInstance(int lancheiraId, String dateLabel) {
        DiaFragment fragment = new DiaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LANCHEIRA_ID, lancheiraId);
        args.putString(ARG_DATE_LABEL, dateLabel);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dia, container, false);

        if (getArguments() != null) {
            lancheiraId = getArguments().getInt(ARG_LANCHEIRA_ID);
            String dateLabel = getArguments().getString(ARG_DATE_LABEL, "Data");
            TextView dateTextView = view.findViewById(R.id.diaTextView);
            dateTextView.setText(dateLabel);
        }

        // Configura o RecyclerView para os alimentos
        RecyclerView recyclerViewAlimentos = view.findViewById(R.id.recyclerViewLancheira);
        recyclerViewAlimentos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtém os alimentos da lancheira e configura o adapter
        List<Alimentos> alimentos = obterAlimentosDaLancheira();
        DiaAlimentosAdapter alimentosAdapter = new DiaAlimentosAdapter(alimentos, getContext());
        recyclerViewAlimentos.setAdapter(alimentosAdapter);

        return view;
    }

    // Método para obter alimentos da lancheira usando o SQLiteHelper
    private List<Alimentos> obterAlimentosDaLancheira() {
        SQLiteHelper dbHelper = SQLiteHelper.getInstance(getContext());
        Lancheira lancheira = dbHelper.obterLancheira(lancheiraId);
        if (lancheira != null) {
            return lancheira.getAlimentos(); // Acessa a lista de alimentos dentro da Lancheira
        } else {
            return new ArrayList<>(); // Retorna uma lista vazia se a lancheira for nula
        }
    }
}
