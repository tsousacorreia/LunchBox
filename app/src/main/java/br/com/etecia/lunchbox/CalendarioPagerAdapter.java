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

    // Método para gerar o rótulo de data para uso no DiaFragment
    public String getDateLabel(int position) {
        int offset = position - daysRange;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, offset);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}