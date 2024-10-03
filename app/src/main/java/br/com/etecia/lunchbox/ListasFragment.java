package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ListasFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listas, container, false);

        // Inicializar ViewPager e TabLayout
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        // Desabilitar swipe no ViewPager
        viewPager.setUserInputEnabled(false);

        // Configurar o Adapter do ViewPager
        ListasPagerAdapter adapter = new ListasPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Conectar o TabLayout com o ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Construtores");
                    break;
                case 1:
                    tab.setText("Reguladores");
                    break;
                case 2:
                    tab.setText("Energéticos");
                    break;
                case 3:
                    tab.setText("Snacks");
                    break;
            }
        }).attach();

        return view;
    }
}