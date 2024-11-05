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

import java.util.List;

public class DiaFragment extends Fragment {

    private static final String ARG_LANCHEIRA_ID = "lancheiraId";
    private static final String ARG_DATE_LABEL = "dateLabel"; // Definindo a constante para o rótulo da data
    private int lancheiraId; // ID da lancheira a ser exibida

    public static DiaFragment newInstance(int lancheiraId, String dateLabel) {
        DiaFragment fragment = new DiaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LANCHEIRA_ID, lancheiraId);
        args.putString(ARG_DATE_LABEL, dateLabel); // Passando o rótulo da data
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dia, container, false);

        // Recupera o ID da lancheira e o rótulo de data
        if (getArguments() != null) {
            lancheiraId = getArguments().getInt(ARG_LANCHEIRA_ID);
            String dateLabel = getArguments().getString(ARG_DATE_LABEL, "Data");
            TextView dateTextView = view.findViewById(R.id.diaTextView);
            dateTextView.setText(dateLabel);
        }

        // Configura o RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLancheira);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtém os alimentos da lancheira e configura o DiaAlimentosAdapter
        List<Alimentos> alimentos = obterAlimentosDaLancheira();
        DiaAlimentosAdapter adapter = new DiaAlimentosAdapter(alimentos, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Método para obter alimentos da lancheira usando o SQLiteHelper
    private List<Alimentos> obterAlimentosDaLancheira() {
        SQLiteHelper dbHelper = SQLiteHelper.getInstance(getContext());
        return dbHelper.obterAlimentosPorLancheira(lancheiraId);
    }
}
