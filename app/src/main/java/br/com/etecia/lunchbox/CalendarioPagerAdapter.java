package br.com.etecia.lunchbox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarioPagerAdapter extends FragmentStateAdapter {

    private final int daysRange;

    public CalendarioPagerAdapter(@NonNull Fragment fragment, int daysRange) {
        super(fragment);
        this.daysRange = daysRange;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int offset = position - daysRange;

        // Obtém a data correspondente ao offset
        String dateLabel = getDateLabel(position);

        // Passa o rótulo da data para o fragmento
        return DiaFragment.newInstance(offset, dateLabel);
    }

    @Override
    public int getItemCount() {
        return daysRange * 2 + 1;
    }

    // Método para obter o rótulo do dia da semana para a posição
    public String getDateLabel(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, position - daysRange);

        String[] weekDays = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom"};
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDays[(dayOfWeek + 5) % 7]; // Ajusta para que Seg seja o primeiro dia
    }
}