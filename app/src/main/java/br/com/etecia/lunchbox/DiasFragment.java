package br.com.etecia.lunchbox;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DiasFragment extends Fragment {

    private static final String ARG_DAY = "day";
    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";

    private int day;
    private int year;
    private int month;

    public static DiasFragment newInstance(int day, int year, int month) {
        DiasFragment fragment = new DiasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY, day);
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getInt(ARG_DAY);
            year = getArguments().getInt(ARG_YEAR);
            month = getArguments().getInt(ARG_MONTH);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dias, container, false);

        // Exiba a data no layout (exemplo de TextView)
        TextView textView = view.findViewById(R.id.textViewDate);
        textView.setText("Data: " + day + "/" + (month + 1) + "/" + year); // +1 porque os meses começam do 0

        return view;
    }
}