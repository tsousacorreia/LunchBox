package br.com.etecia.lunchbox;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnAlimentoSelectedListener {

    private BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        //referente ao login
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //button = findViewById(R.id.btnLogout);
        //textView = findViewById(R.id.userDetails);

        //verificar se tem alguem logado
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
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

    // Novo metodo para selecionar a lancheira
    public void onSelectLancheira() {
        bottomNavigationView.setSelectedItemId(R.id.nav_lancheira);
    }
}