package br.com.etecia.lunchbox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CalendarioPagerAdapter extends FragmentStateAdapter {
    private final int numDays;
    private final int year;
    private final int month;
    private final SharedViewModel sharedViewModel;

    public CalendarioPagerAdapter(@NonNull FragmentActivity fragmentActivity, int numDays, int year, int month, SharedViewModel sharedViewModel) {
        super(fragmentActivity);
        this.numDays = numDays;
        this.year = year;
        this.month = month;
        this.sharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Verifique se o LancheiraDiaFragment está implementado
        return new LancheiraDiaFragment(year, month, position + 1, sharedViewModel);
    }

    @Override
    public int getItemCount() {
        return numDays; // Número de dias no mês
    }
}