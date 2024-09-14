package br.com.etecia.lunchbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements OnAlimentoSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Carregando os fragmentos no layout
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Verifica se já há um estado salvo (para evitar adicionar fragmentos novamente)
        if (savedInstanceState == null) {
            // Carrega o fragmento com as listas de alimentos (Construtores, Reguladores, Energeticos)
            fragmentManager.beginTransaction()
                    .replace(R.id.listas_fragment_container, new ListasFragment())
                    .commit();

            // Carrega o fragmento da lancheira
            fragmentManager.beginTransaction()
                    .replace(R.id.lancheira_fragment_container, new LancheiraFragment())
                    .commit();
        }
    }

    @Override
    public void onAlimentoSelected(FoodItem alimento) {
        // Implementação do que fazer quando um alimento for selecionado.
        // Exemplo: Adicionar o alimento à lancheira
        LancheiraFragment lancheiraFragment = (LancheiraFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lancheira_fragment_container);

        if (lancheiraFragment != null) {
            // Adiciona o alimento à lancheira
            lancheiraFragment.adicionarAlimentoALancheira(alimento);
        } else {
            // Exibe um erro ou toma uma ação alternativa, se necessário
            // Exemplo: Log de erro ou criação do fragmento se ele estiver nulo
        }
    }
}