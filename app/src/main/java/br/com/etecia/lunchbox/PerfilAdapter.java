package br.com.etecia.lunchbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.PerfilViewHolder> {

    private List<Perfil> perfis;
    private PerfilClickListener perfilClickListener;
    private int expandedPosition = -1;

    public interface PerfilClickListener {
        void onPerfilClick(Perfil perfil);

        void onSelecionarClick(Perfil perfil);
    }

    public PerfilAdapter(List<Perfil> perfis, PerfilClickListener perfilClickListener) {
        this.perfis = perfis;
        this.perfilClickListener = perfilClickListener;
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
        holder.textIdadePerfil.setText("Idade: " + perfil.getIdade());
        holder.textPreferenciasPerfil.setText(perfil.getPreferencias());

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition(); // Usar getAdapterPosition() para obter a posição atual
            if (expandedPosition == adapterPosition) {
                collapse(holder);
                expandedPosition = -1;
            } else {
                if (expandedPosition >= 0) {
                    notifyItemChanged(expandedPosition);
                }
                expandedPosition = adapterPosition;
                notifyItemChanged(adapterPosition);
            }
        });

        holder.btnSelecionar.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                perfilClickListener.onSelecionarClick(perfis.get(adapterPosition));
            }
        });

        if (holder.getAdapterPosition() == expandedPosition) {
            expand(holder);
        } else {
            collapse(holder);
        }
    }

    @Override
    public int getItemCount() {
        return perfis.size();
    }

    private void expand(PerfilViewHolder holder) {
        holder.containerPreferencias.setVisibility(View.VISIBLE);
        holder.containerPreferencias.setAlpha(0f);
        holder.containerPreferencias.animate().alpha(1f).setDuration(300).start();
    }

    private void collapse(PerfilViewHolder holder) {
        holder.containerPreferencias.animate().alpha(0f).setDuration(300).withEndAction(() -> {
            holder.containerPreferencias.setVisibility(View.GONE);
        }).start();
    }

    public class PerfilViewHolder extends RecyclerView.ViewHolder {
        TextView textNomePerfil, textIdadePerfil, textPreferenciasPerfil;
        LinearLayout containerPreferencias;
        Button btnSelecionar;

        public PerfilViewHolder(View itemView) {
            super(itemView);
            textNomePerfil = itemView.findViewById(R.id.text_nome_perfil);
            textIdadePerfil = itemView.findViewById(R.id.text_idade_perfil);
            textPreferenciasPerfil = itemView.findViewById(R.id.text_preferencias_perfil);
            containerPreferencias = itemView.findViewById(R.id.container_preferencias);
            btnSelecionar = itemView.findViewById(R.id.btn_selecionar);
        }
    }
}