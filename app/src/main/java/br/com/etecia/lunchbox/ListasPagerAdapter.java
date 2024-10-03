package br.com.etecia.lunchbox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ListasPagerAdapter extends FragmentStateAdapter {

    public ListasPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ConstrutoresFragment();
            case 1:
                return new ReguladoresFragment();
            case 2:
                return new EnergeticosFragment();
            case 3:
                return new SnacksFragment();
            default:
                return new ConstrutoresFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}