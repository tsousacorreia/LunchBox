package br.com.etecia.lunchbox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CalendarioPagerAdapter extends FragmentStateAdapter {

    private final int numDays;
    private final int year;
    private final int month;

    public CalendarioPagerAdapter(@NonNull FragmentActivity fragmentActivity, int numDays, int year, int month) {
        super(fragmentActivity);
        this.numDays = numDays;
        this.year = year;
        this.month = month;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Retorne o Fragment correspondente à aba (dia) e passe a data para ele
        return DiasFragment.newInstance(position + 1, year, month);
    }

    @Override
    public int getItemCount() {
        return numDays; // Número total de dias no mês
    }
}