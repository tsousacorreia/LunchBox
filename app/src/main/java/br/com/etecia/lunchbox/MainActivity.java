package br.com.etecia.lunchbox;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnAlimentoSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_alimentos) {
                selectedFragment = new ListasFragment();
            } else if (item.getItemId() == R.id.nav_lancheira) {
                selectedFragment = new LancheiraFragment();
            } else if (item.getItemId() == R.id.nav_calendario) {
                selectedFragment = new CalendarioFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    public void onAlimentoSelected(Alimentos alimento) {
        // Não navega automaticamente para a lancheira aqui.
    }

    @Override
    public void onVisualizarLancheira() {
        LancheiraFragment lancheiraFragment = new LancheiraFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, lancheiraFragment)
                .addToBackStack(null)
                .commit();
    }
}