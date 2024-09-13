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
                return new ConstrutoresFragment(); // Categoria Construtores
            case 1:
                return new ReguladoresFragment(); // Categoria Reguladores
            case 2:
                return new EnergeticosFragment(); // Categoria Energéticos
            default:
                return new ConstrutoresFragment(); // Caso padrão
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Três categorias de alimentos
    }
}