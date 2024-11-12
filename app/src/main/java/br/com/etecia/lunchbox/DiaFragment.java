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
import android.widget.TextView;

import java.util.List;

public class DiaFragment extends Fragment {

    private static final String ARG_DATE_LABEL = "dateLabel";
    private TextView diaTextView;
    private RecyclerView recyclerViewLancheira;
    private DiaLancheiraAdapter diaLancheiraAdapter;

    // Método para criar uma nova instância do fragmento
    public static DiaFragment newInstance(int offset, String dateLabel) {
        DiaFragment fragment = new DiaFragment();
        Bundle args = new Bundle();
        args.putInt("offset", offset);
        args.putString(ARG_DATE_LABEL, dateLabel);  // Passando a data para o fragmento
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dia, container, false);

        diaTextView = view.findViewById(R.id.diaTextView);
        recyclerViewLancheira = view.findViewById(R.id.recyclerViewLancheira);
        recyclerViewLancheira.setLayoutManager(new LinearLayoutManager(getContext()));

        // Recupera o label da data e define no TextView
        if (getArguments() != null) {
            String dateLabel = getArguments().getString(ARG_DATE_LABEL);
            if (dateLabel != null) {
                diaTextView.setText(dateLabel);  // Atualizando o TextView com o valor da data
            }
        }

        carregarLancheiras();

        return view;
    }

    private void carregarLancheiras() {
        if (getContext() == null) return;

        SQLiteHelper dbHelper = SQLiteHelper.getInstance(getContext());
        String dateLabel = getArguments() != null ? getArguments().getString(ARG_DATE_LABEL) : null;

        if (dateLabel != null) {
            List<Lancheira> lancheiras = dbHelper.obterLancheirasPorData(dateLabel);
            diaLancheiraAdapter = new DiaLancheiraAdapter(getContext(), lancheiras, dateLabel);
            recyclerViewLancheira.setAdapter(diaLancheiraAdapter);
        }
    }
}