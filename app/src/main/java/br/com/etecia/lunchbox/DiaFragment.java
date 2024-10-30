package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.TextView;

public class DiaFragment extends Fragment {

    private static final String ARG_OFFSET = "offset";
    private static final String ARG_DATE_LABEL = "dateLabel";

    public static DiaFragment newInstance(int offset, String dateLabel) {
        DiaFragment fragment = new DiaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OFFSET, offset);
        args.putString(ARG_DATE_LABEL, dateLabel);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dia, container, false);

        // Recupera o rótulo de data e exibe no TextView (ou em outro elemento)
        String dateLabel = getArguments() != null ? getArguments().getString(ARG_DATE_LABEL) : "";
        TextView dateTextView = view.findViewById(R.id.diaTextView);
        dateTextView.setText(dateLabel);

        return view;
    }
}