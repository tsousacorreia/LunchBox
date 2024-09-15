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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listas, container, false);

        // Configurando o ViewPager2 e TabLayout para as categorias de alimentos
        ViewPager2 viewPager = view.findViewById(R.id.viewpager2);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        // Adicionar adaptador ao ViewPager2
        viewPager.setAdapter(new ListasPagerAdapter(this));

        // Associar o TabLayout ao ViewPager2 usando TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
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
                    }
                }).attach();

        return view;
    }
}