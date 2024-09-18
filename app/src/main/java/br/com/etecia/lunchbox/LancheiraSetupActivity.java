package br.com.etecia.lunchbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LancheiraSetupActivity extends AppCompatActivity {

    private CardView cardCriarNovoPerfil, cardPerfil, cardPerfisExistentes;
    private EditText editNome, editIdade, editPreferencias;
    private Button btnCriarPerfil;
    private RecyclerView recyclerViewPerfis;

    private PerfilAdapter perfilAdapter;
    private List<Perfil> perfis;
    private Perfil perfilSelecionado;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancheira_setup);

        // Inicializando as views
        cardCriarNovoPerfil = findViewById(R.id.card_criar_novo_perfil);
        cardPerfil = findViewById(R.id.card_perfil);
        cardPerfisExistentes = findViewById(R.id.card_perfis_existentes);
        editNome = findViewById(R.id.edit_nome);
        editIdade = findViewById(R.id.edit_idade);
        editPreferencias = findViewById(R.id.edit_preferencias);
        btnCriarPerfil = findViewById(R.id.btn_criar_perfil);
        recyclerViewPerfis = findViewById(R.id.recycler_view_perfis);

        // Inicializa a lista de perfis e o adapter
        perfis = new ArrayList<>();
        perfilAdapter = new PerfilAdapter(perfis, new PerfilAdapter.PerfilClickListener() {
            @Override
            public void onPerfilClick(Perfil perfil) {
                perfilSelecionado = perfil;
                Toast.makeText(LancheiraSetupActivity.this, "Perfil selecionado: " + perfil.getNome(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelecionarClick(Perfil perfil) {
                perfilSelecionado = perfil;
                abrirMontagemLancheira(perfilSelecionado);
            }
        });
        recyclerViewPerfis.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPerfis.setAdapter(perfilAdapter);

        // Inicializa o DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Recuperar perfis existentes do banco de dados local
        carregarPerfis();

        // Lógica para criar o perfil
        btnCriarPerfil.setOnClickListener(v -> {
            if (validateInputs()) {
                criarPerfil();
            }
        });

        // Lógica para exibir o card de criação de perfil ao clicar no cardCriarNovoPerfil
        cardCriarNovoPerfil.setOnClickListener(v -> {
            toggleCardVisibility(cardCriarNovoPerfil, cardPerfil);
        });
    }

    // Método para carregar perfis do banco de dados local
    private void carregarPerfis() {
        perfis.clear();
        perfis.addAll(databaseHelper.getPerfis());
        perfilAdapter.notifyDataSetChanged();
    }

    // Método para criar um perfil e salvar no banco de dados local
    private void criarPerfil() {
        String nome = editNome.getText().toString();
        int idade;
        try {
            idade = Integer.parseInt(editIdade.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(LancheiraSetupActivity.this, "Idade inválida", Toast.LENGTH_SHORT).show();
            return;
        }
        String preferencias = editPreferencias.getText().toString();

        long id = databaseHelper.addPerfil(nome, idade, preferencias);
        if (id != -1) {
            Perfil novoPerfil = new Perfil((int) id, nome, idade, preferencias);
            perfis.add(novoPerfil);
            perfilAdapter.notifyDataSetChanged();
            Toast.makeText(LancheiraSetupActivity.this, "Perfil criado com sucesso!", Toast.LENGTH_SHORT).show();
            toggleCardVisibility(cardPerfil, cardPerfisExistentes);
        } else {
            Toast.makeText(LancheiraSetupActivity.this, "Erro ao criar perfil", Toast.LENGTH_SHORT).show();
        }
    }


    // Método para abrir a MainActivity com os dados do perfil selecionado
    private void abrirMontagemLancheira(Perfil perfilSelecionado) {
        Intent intent = new Intent(LancheiraSetupActivity.this, MainActivity.class);
        intent.putExtra("perfil_id", perfilSelecionado.getId());
        intent.putExtra("perfil_nome", perfilSelecionado.getNome());
        intent.putExtra("perfil_preferencias", perfilSelecionado.getPreferencias());
        startActivity(intent);
    }

    // Validação de entrada dos campos de perfil
    private boolean validateInputs() {
        if (editNome.getText().toString().isEmpty() || editIdade.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método para alternar visibilidade entre os cards com animação
    private void toggleCardVisibility(final CardView fromCard, final CardView toCard) {
        // Animação de fade-out
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(500);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fromCard.setVisibility(View.GONE);
                // Animação de fade-in
                toCard.setVisibility(View.VISIBLE);
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(500);
                toCard.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        fromCard.startAnimation(fadeOut);
    }
}