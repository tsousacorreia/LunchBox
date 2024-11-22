package br.com.etecia.lunchbox.lunchbox;

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
import java.util.Calendar;

import br.com.etecia.lunchbox.R;

public class CalendarioFragment extends Fragment {

    private ViewPager2 viewPager;
    private CalendarioPagerAdapter viewPagerAdapter;
    private static final int DAYS_RANGE = 90;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        viewPager = view.findViewById(R.id.viewPager);

        // Botão para abrir o DatePicker
        Button buttonSelectDate = view.findViewById(R.id.buttonSelectDate);
        buttonSelectDate.setOnClickListener(v -> openDatePicker());

        setupViewPager();

        return view;
    }

    private void setupViewPager() {
        viewPagerAdapter = new CalendarioPagerAdapter(this, DAYS_RANGE);
        viewPager.setAdapter(viewPagerAdapter);

        // Iniciar no dia atual
        viewPager.setCurrentItem(DAYS_RANGE, false);
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> navigateToSelectedDate(year, month, dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void navigateToSelectedDate(int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);

        Calendar today = Calendar.getInstance();
        int daysDifference = (int) ((selectedDate.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24));

        int targetPosition = DAYS_RANGE + daysDifference;

        if (targetPosition >= 0 && targetPosition < viewPagerAdapter.getItemCount()) {
            viewPager.setCurrentItem(targetPosition, true);
        }
    }
}