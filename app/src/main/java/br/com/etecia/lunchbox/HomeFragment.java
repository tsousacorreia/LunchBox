package br.com.etecia.lunchbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;


import java.util.List;

public class HomeFragment extends Fragment {

    private CardView cardCriarNovoPerfil, cardPerfil, cardPerfisExistentes;
    private EditText editNome, editIdade, editPreferencias;
    private Button btnCriarPerfil;
    private RecyclerView recyclerViewPerfis;
    private PerfilAdapter perfilAdapter;
    private List<Perfil> perfis;
    private PerfilDAO perfilDAO;
    private PerfilViewModel perfilViewModel;
    FirebaseAuth auth;
    Button button;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializando as views
        cardCriarNovoPerfil = view.findViewById(R.id.card_criar_novo_perfil);
        cardPerfil = view.findViewById(R.id.card_perfil);
        cardPerfisExistentes = view.findViewById(R.id.card_perfis_existentes);
        editNome = view.findViewById(R.id.edit_nome);
        editIdade = view.findViewById(R.id.edit_idade);
        editPreferencias = view.findViewById(R.id.edit_preferencias);
        btnCriarPerfil = view.findViewById(R.id.btn_criar_perfil);
        recyclerViewPerfis = view.findViewById(R.id.recycler_view_perfis);
        button = view.findViewById(R.id.btnLogout);
        auth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                try {
                    finalize();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });


        // Inicializar o DAO
        perfilDAO = new PerfilDAO(getActivity());

        // Carregar os perfis salvos no banco de dados
        perfis = perfilDAO.listarPerfis();

        // Verificar se há perfis e exibir/ocultar os cards correspondentes
        atualizarVisibilidadeCards();

        // Inicializa o ViewModel
        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

        // Inicializa o adapter
        perfilAdapter = new PerfilAdapter(perfis, new PerfilAdapter.PerfilClickListener() {
            @Override
            public void onPerfilClick(Perfil perfil) {
                // Aqui não fazemos nada, apenas um click simples
            }

            @Override
            public void onSelecionarClick(Perfil perfil) {
                perfilViewModel.setPerfilSelecionado(perfil);
                abrirListasFragment();
            }
        }, getActivity());

        recyclerViewPerfis.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPerfis.setAdapter(perfilAdapter);

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

        return view;
    }

    // Abrir o ListasFragment
    private void abrirListasFragment() {
        ListasFragment listasFragment = new ListasFragment();

        // Substituindo o HomeFragment pelo ListasFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listasFragment)
                .addToBackStack(null)
                .commit();

        // Atualizar o BottomNavigationView após a troca de fragmento
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_alimentos);
    }

    // Validação de entrada dos campos de perfil
    private boolean validateInputs() {
        if (editNome.getText().toString().isEmpty() || editIdade.getText().toString().isEmpty() || editPreferencias.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Alternar visibilidade entre os cards com animação
    private void toggleCardVisibility(final CardView fromCard, final CardView toCard) {
        fromCard.setVisibility(View.GONE);
        toCard.setVisibility(View.VISIBLE);
    }

    private void criarPerfil() {
        String nome = editNome.getText().toString();
        int idade = Integer.parseInt(editIdade.getText().toString());
        String preferencias = editPreferencias.getText().toString();

        // Criar o objeto perfil
        Perfil novoPerfil = new Perfil(nome, idade, preferencias);

        // Inserir no banco de dados
        long id = perfilDAO.inserirPerfil(novoPerfil);

        if (id > 0) {
            novoPerfil.setId((int) id);
            perfis.add(novoPerfil);
            perfilAdapter.notifyDataSetChanged();

            editNome.setText("");
            editIdade.setText("");
            editPreferencias.setText("");

            atualizarVisibilidadeCards();

            cardPerfil.setVisibility(View.GONE);
            cardCriarNovoPerfil.setVisibility(View.VISIBLE);

            Toast.makeText(getActivity(), "Perfil criado com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Erro ao criar perfil!", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarVisibilidadeCards() {
        if (perfis.isEmpty()) {
            cardCriarNovoPerfil.setVisibility(View.VISIBLE);
            cardPerfisExistentes.setVisibility(View.GONE);
        } else {
            cardCriarNovoPerfil.setVisibility(View.VISIBLE);
            cardPerfisExistentes.setVisibility(View.VISIBLE);
        }
    }

    public void deletarPerfil(Perfil perfil) {
        perfilDAO.deletarPerfil(perfil.getId());
        perfis.remove(perfil);
        perfilAdapter.notifyDataSetChanged();

        atualizarVisibilidadeCards();
    }

}