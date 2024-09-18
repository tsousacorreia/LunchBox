package br.com.etecia.lunchbox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnAlimentoSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura o Top App Bar (Toolbar)
        Toolbar toolbar = findViewById(R.id.top_app_bar);
        setSupportActionBar(toolbar);

        // Suporte à navegação de volta (se necessário)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("MainActivity", "ActionBar não encontrada");
        }

        // Configuração do Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNavigation);

        // Verifica se há dados da Intent enviada pela LancheiraSetupActivity
        if (savedInstanceState == null) {
            handleIncomingIntent(getIntent());
        }
    }

    // Método para tratar a navegação do BottomNavigationView
    private boolean handleBottomNavigation(MenuItem item) {
        int itemId = item.getItemId();  // Obtém o ID do item selecionado

        if (itemId == R.id.nav_home) {
            // Navega para a LancheiraSetupActivity (que será a nova página inicial)
            Intent intentHome = new Intent(MainActivity.this, LancheiraSetupActivity.class);
            startActivity(intentHome);
        } else if (itemId == R.id.nav_lancheira) {
            // Exibe o fragmento da lancheira no container
            LancheiraFragment lancheiraFragment = new LancheiraFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.lancheira_fragment_container, lancheiraFragment)
                    .commit();
        } else if (itemId == R.id.nav_calendario) {
            // Navega para a CalendarioLancheiraActivity
            Intent intentCalendario = new Intent(MainActivity.this, CalendarioLancheiraActivity.class);
            startActivity(intentCalendario);
        }

        return true;  // Retorna true para indicar que o item foi selecionado
    }

    // Método para tratar os dados recebidos da LancheiraSetupActivity
    private void handleIncomingIntent(Intent intent) {
        int perfilId = intent.getIntExtra("perfil_id", -1);
        String perfilNome = intent.getStringExtra("perfil_nome");
        String perfilPreferencias = intent.getStringExtra("perfil_preferencias");

        if (perfilId != -1 && perfilNome != null && perfilPreferencias != null) {
            // Cria o LancheiraFragment com os argumentos
            LancheiraFragment lancheiraFragment = new LancheiraFragment();
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putInt("perfil_id", perfilId);
            fragmentArgs.putString("perfil_nome", perfilNome);
            fragmentArgs.putString("perfil_preferencias", perfilPreferencias);
            lancheiraFragment.setArguments(fragmentArgs);

            // Adiciona os fragmentos em uma única transação
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.listas_fragment_container, new ListasFragment())
                    .replace(R.id.lancheira_fragment_container, lancheiraFragment)
                    .commit();
        } else {
            // Exibe uma mensagem de erro caso os dados não sejam válidos
            Toast.makeText(this, "Erro ao carregar dados do perfil", Toast.LENGTH_SHORT).show();
        }
    }

    // Implementa a seleção de alimentos
    @Override
    public void onAlimentoSelected(Alimentos alimento) {
        LancheiraFragment lancheiraFragment = (LancheiraFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lancheira_fragment_container);

        // Verifica se o fragmento não é nulo
        if (lancheiraFragment != null) {
            lancheiraFragment.adicionarAlimentoALancheira(alimento);
        }
    }

    // Implementa a navegação de volta no Top App Bar
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
