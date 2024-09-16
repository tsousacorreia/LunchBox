package br.com.etecia.lunchbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements OnAlimentoSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            // Recebe os dados da Intent enviada pela LancheiraSetupActivity
            Bundle bundle = getIntent().getExtras();
            String nomeLancheira = bundle.getString("nome_lancheira");
            String dataLancheira = bundle.getString("data_lancheira");
            int perfilId = bundle.getInt("perfil_id");

            // Cria o LancheiraFragment com os argumentos
            LancheiraFragment lancheiraFragment = new LancheiraFragment();
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putString("nome_lancheira", nomeLancheira);
            fragmentArgs.putString("data_lancheira", dataLancheira);
            fragmentArgs.putInt("perfil_id", perfilId);
            lancheiraFragment.setArguments(fragmentArgs);

            // Adiciona os fragmentos
            fragmentManager.beginTransaction()
                    .replace(R.id.listas_fragment_container, new ListasFragment())
                    .commit();

            fragmentManager.beginTransaction()
                    .replace(R.id.lancheira_fragment_container, lancheiraFragment)
                    .commit();
        }
    }

    @Override
    public void onAlimentoSelected(Alimentos alimento) {
        LancheiraFragment lancheiraFragment = (LancheiraFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lancheira_fragment_container);

        if (lancheiraFragment != null) {
            lancheiraFragment.adicionarAlimentoALancheira(alimento);
        }
    }
}