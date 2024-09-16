package br.com.etecia.lunchbox;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LancheiraSetupActivity extends AppCompatActivity {

    private CardView cardPerfil, cardDiaSemana, cardPerfisExistentes;
    private EditText editNome, editIdade, editPreferencias;
    private Button btnCriarPerfil, btnFinalizarDia;
    private RadioGroup radioGroupDias;
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
        cardPerfil = findViewById(R.id.card_perfil);
        cardDiaSemana = findViewById(R.id.card_dia_semana);
        cardPerfisExistentes = findViewById(R.id.card_perfis_existentes);
        editNome = findViewById(R.id.edit_nome);
        editIdade = findViewById(R.id.edit_idade);
        editPreferencias = findViewById(R.id.edit_preferencias);
        btnCriarPerfil = findViewById(R.id.btn_criar_perfil);
        radioGroupDias = findViewById(R.id.radio_group_dias);
        btnFinalizarDia = findViewById(R.id.btn_finalizar_dia);
        recyclerViewPerfis = findViewById(R.id.recycler_view_perfis);

        // Inicializa a lista de perfis e o adapter
        perfis = new ArrayList<>();
        perfilAdapter = new PerfilAdapter(perfis, perfil -> {
            // Lógica para seleção de perfil existente
            perfilSelecionado = perfil;  // Guarda o perfil selecionado
            Toast.makeText(this, "Perfil selecionado: " + perfil.getNome(), Toast.LENGTH_SHORT).show();
            toggleCardVisibility(cardPerfisExistentes, cardDiaSemana);
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

        // Lógica para finalizar a seleção do dia da semana
        btnFinalizarDia.setOnClickListener(v -> {
            int selectedDayId = radioGroupDias.getCheckedRadioButtonId();
            if (selectedDayId != -1) {
                String diaSelecionado = getSelectedDay(selectedDayId);
                finalizarSelecaoDia(diaSelecionado);
            } else {
                Toast.makeText(this, "Por favor, selecione um dia.", Toast.LENGTH_SHORT).show();
            }
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
        int idade = Integer.parseInt(editIdade.getText().toString());
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

    // Método para finalizar a seleção do dia e avançar para a montagem da lancheira
    private void finalizarSelecaoDia(String dia) {
        if (perfilSelecionado != null) {
            // Passa os dados para a MainActivity sem salvar no banco de dados ainda
            Intent intent = new Intent(LancheiraSetupActivity.this, MainActivity.class);
            intent.putExtra("nome_lancheira", "Lancheira de " + perfilSelecionado.getNome());
            intent.putExtra("data_lancheira", dia);
            intent.putExtra("perfil_id", perfilSelecionado.getId());
            startActivity(intent);
            finish();  // Fecha a LancheiraSetupActivity após abrir a MainActivity

            Toast.makeText(LancheiraSetupActivity.this, "Dia da semana selecionado!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Por favor, selecione um perfil.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para obter o dia selecionado no RadioGroup
    private String getSelectedDay(int selectedDayId) {
        RadioButton selectedRadioButton = findViewById(selectedDayId);
        return selectedRadioButton.getText().toString();
    }

    // Validação de entrada dos campos de perfil
    private boolean validateInputs() {
        if (editNome.getText().toString().isEmpty() || editIdade.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método para alternar visibilidade entre os cards
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