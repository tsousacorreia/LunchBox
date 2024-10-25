package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarioFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendario_lancheira, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tab_Layout_calendar);

        // Inicializa o ViewModel para compartilhar dados
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Configura o adapter do ViewPager2
        CalendarioPagerAdapter adapter = new CalendarioPagerAdapter(requireActivity(), numDays, year, month, sharedViewModel);
        viewPager.setAdapter(adapter);

        // Define o TabLayout com os dias da semana
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            calendar.set(year, month, position + 1);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault()); // Formato do dia da semana
            tab.setText(sdf.format(calendar.getTime())); // Nome do dia da semana
        }).attach();
    }
}