package br.com.etecia.lunchbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.PerfilViewHolder> {

    private final List<Perfil> perfis;
    private final PerfilClickListener perfilClickListener;

    public PerfilAdapter(List<Perfil> perfis, PerfilClickListener perfilClickListener) {
        this.perfis = perfis;
        this.perfilClickListener = perfilClickListener;
    }

    @NonNull
    @Override
    public PerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_perfil, parent, false);
        return new PerfilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerfilViewHolder holder, int position) {
        Perfil perfil = perfis.get(position);
        holder.bind(perfil);
    }

    @Override
    public int getItemCount() {
        return perfis.size();
    }

    public interface PerfilClickListener {
        void onPerfilClick(Perfil perfil);
    }

    class PerfilViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtNomePerfil;
        private final TextView txtIdadePerfil;
        private final TextView txtPreferenciasPerfil;

        public PerfilViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomePerfil = itemView.findViewById(R.id.text_nome_perfil);
            txtIdadePerfil = itemView.findViewById(R.id.text_idade_perfil);
            txtPreferenciasPerfil = itemView.findViewById(R.id.text_preferencias_perfil);

            itemView.setOnClickListener(v -> {
                Perfil perfil = perfis.get(getAdapterPosition());
                perfilClickListener.onPerfilClick(perfil);
            });
        }

        public void bind(Perfil perfil) {
            txtNomePerfil.setText(perfil.getNome());
            txtIdadePerfil.setText(String.valueOf(perfil.getIdade()));
            txtPreferenciasPerfil.setText(perfil.getPreferencias());
        }
    }
}