package br.com.etecia.lunchbox;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText; // Importando EditText
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.PerfilViewHolder> {

    private List<Perfil> perfis;
    private PerfilClickListener perfilClickListener;
    private Context context; // Para acesso ao contexto
    private PerfilDAO perfilDAO; // Para operações de banco de dados

    public interface PerfilClickListener {
        void onPerfilClick(Perfil perfil);
        void onSelecionarClick(Perfil perfil);
    }

    public PerfilAdapter(List<Perfil> perfis, PerfilClickListener perfilClickListener, Context context) {
        this.perfis = perfis;
        this.perfilClickListener = perfilClickListener;
        this.context = context; // Inicializa o contexto
        this.perfilDAO = new PerfilDAO(context); // Inicializa o DAO
    }

    @Override
    public PerfilViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_perfil, parent, false);
        return new PerfilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PerfilViewHolder holder, int position) {
        Perfil perfil = perfis.get(position);

        holder.textNomePerfil.setText(perfil.getNome());
        holder.textIdadePerfil.setText("Idade: " + perfil.getIdade() + " anos");
        holder.textPreferenciasPerfil.setText(perfil.getPreferencias());

        holder.itemView.setOnClickListener(v -> {
            perfilClickListener.onPerfilClick(perfil);
        });

        holder.btnOpcoesPerfil.setOnClickListener(v -> showOptionsDialog(perfil, position));
    }

    @Override
    public int getItemCount() {
        return perfis.size();
    }

    private void showOptionsDialog(Perfil perfil, int position) {
        String[] options = {"Editar", "Excluir"};
        new AlertDialog.Builder(context)
                .setTitle("Escolha uma opção")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Editar
                            showEditDialog(perfil);
                            break;
                        case 1: // Excluir
                            showDeleteConfirmationDialog(perfil, position);
                            break;
                    }
                })
                .show();
    }

    private void showEditDialog(Perfil perfil) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_perfil, null);
        builder.setView(dialogView);

        EditText editNome = dialogView.findViewById(R.id.edit_nome);
        EditText editIdade = dialogView.findViewById(R.id.edit_idade);
        EditText editPreferencias = dialogView.findViewById(R.id.edit_preferencias); // Campo de preferências

        // Preenche os campos com os dados do perfil existente
        editNome.setText(perfil.getNome());
        editIdade.setText(String.valueOf(perfil.getIdade()));
        editPreferencias.setText(perfil.getPreferencias()); // Preenche o campo de preferências

        builder.setTitle("Editar Perfil")
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String nome = editNome.getText().toString();
                    int idade = Integer.parseInt(editIdade.getText().toString());
                    String preferencias = editPreferencias.getText().toString(); // Captura as preferências

                    perfil.setNome(nome);
                    perfil.setIdade(idade);
                    perfil.setPreferencias(preferencias); // Atualiza as preferências

                    perfilDAO.atualizarPerfil(perfil);
                    notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    private void showDeleteConfirmationDialog(Perfil perfil, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmação")
                .setMessage("Tem certeza que deseja excluir " + perfil.getNome() + "?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    perfilDAO.deletarPerfil(perfil.getId());
                    perfis.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton("Não", null)
                .show();
    }

    public class PerfilViewHolder extends RecyclerView.ViewHolder {
        TextView textNomePerfil, textIdadePerfil, textPreferenciasPerfil;
        LinearLayout containerPreferencias;
        ImageButton btnOpcoesPerfil; // Adiciona o botão de opções

        public PerfilViewHolder(View itemView) {
            super(itemView);
            textNomePerfil = itemView.findViewById(R.id.text_nome_perfil);
            textIdadePerfil = itemView.findViewById(R.id.text_idade_perfil);
            textPreferenciasPerfil = itemView.findViewById(R.id.text_preferencias_perfil);
            containerPreferencias = itemView.findViewById(R.id.container_preferencias);
            btnOpcoesPerfil = itemView.findViewById(R.id.btn_opcoes_perfil);
        }
    }
}