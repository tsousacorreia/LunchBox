package br.com.etecia.lunchbox.lunchbox;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.etecia.lunchbox.R;

public class MainActivity extends AppCompatActivity implements OnAlimentoSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private long backPressedTime;
    private FirebaseAuth auth;
    private Button button;
    private TextView textView;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
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

        // Atualizar o BottomNavigationView para a aba da lancheira
        bottomNavigationView.setSelectedItemId(R.id.nav_lancheira);
    }

    @Override
    public void onVisualizarCalendario() {
        CalendarioFragment calendarioFragment = new CalendarioFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, calendarioFragment)
                .addToBackStack(null)
                .commit();

        // Atualizar o BottomNavigationView para a aba da lancheira
        bottomNavigationView.setSelectedItemId(R.id.nav_calendario);
    }

    @Override
    public void onBackPressed() {
        // Se o tempo entre os pressionamentos for menor que 2 segundos, sai do app
        if (backPressedTime + 2500 > System.currentTimeMillis()) {
            super.onBackPressed(); // Chama a ação padrão (sair do app)
            return;
        } else {
            // Caso contrário, exibe a mensagem e atualiza o tempo
            Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();
        }

        // Atualiza o tempo do último pressionamento
        backPressedTime = System.currentTimeMillis();
    }

    public void onSelectLancheira() {
        bottomNavigationView.setSelectedItemId(R.id.nav_lancheira);
    }

    public void onSelectCalendario() {
        bottomNavigationView.setSelectedItemId(R.id.nav_calendario);
    }
}