package br.com.etecia.lunchbox;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Calendar;

public class CalendarioFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CalendarioPagerAdapter viewPagerAdapter;
    private static final int DAYS_RANGE = 30;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // Botão para abrir o DatePicker
        Button buttonSelectDate = view.findViewById(R.id.buttonSelectDate);
        buttonSelectDate.setOnClickListener(v -> openDatePicker());

        setupViewPagerWithTabLayout();

        return view;
    }

    private void setupViewPagerWithTabLayout() {
        viewPagerAdapter = new CalendarioPagerAdapter(this, DAYS_RANGE);
        viewPager.setAdapter(viewPagerAdapter);

        // Definindo nomes fixos dos dias da semana no TabLayout
        String[] weekDays = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom"};
        for (String day : weekDays) {
            tabLayout.addTab(tabLayout.newTab().setText(day));
        }

        // Listener para atualizar o TabLayout conforme o ViewPager navega
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int dayOfWeek = getDayOfWeekForPosition(position);
                TabLayout.Tab tab = tabLayout.getTabAt(dayOfWeek);
                if (tab != null) {
                    tab.select();
                }
            }
        });

        // Iniciar no dia atual
        viewPager.setCurrentItem(DAYS_RANGE, false);
    }

    private int getDayOfWeekForPosition(int position) {
        // Cálculo do dia da semana para a posição no ViewPager
        Calendar calendar = Calendar.getInstance();
        int offset = position - DAYS_RANGE;
        calendar.add(Calendar.DAY_OF_YEAR, offset);

        // Ajustando para nosso array de dias (Seg = 0, ..., Dom = 6)
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek == Calendar.SUNDAY) ? 6 : dayOfWeek - 2;
    }

    private void openDatePicker() {
        // Obtém a data atual
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> navigateToSelectedDate(year, month, dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void navigateToSelectedDate(int year, int month, int dayOfMonth) {
        // Calcula o índice no ViewPager com base na data selecionada
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);

        Calendar today = Calendar.getInstance();
        int daysDifference = (int) ((selectedDate.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24));

        // Calcula a posição relativa no ViewPager
        int targetPosition = DAYS_RANGE + daysDifference;

        // Move o ViewPager para a posição
        if (targetPosition >= 0 && targetPosition < viewPagerAdapter.getItemCount()) {
            viewPager.setCurrentItem(targetPosition, true);
        }
    }
}