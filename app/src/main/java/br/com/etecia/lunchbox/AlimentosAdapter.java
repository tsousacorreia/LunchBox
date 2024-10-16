package br.com.etecia.lunchbox;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AlimentosAdapter extends RecyclerView.Adapter<AlimentosAdapter.AlimentoViewHolder> {

    private List<Alimentos> alimentos = new ArrayList<>();
    private SharedViewModel sharedViewModel;
    private OnAlimentoClickListener listener;

    public AlimentosAdapter(SharedViewModel sharedViewModel, OnAlimentoClickListener listener) {
        this.sharedViewModel = sharedViewModel;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlimentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_alimento, parent, false);
        return new AlimentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentoViewHolder holder, int position) {
        Alimentos alimento = alimentos.get(position);

        // Preenche as informações do alimento
        holder.textFoodName.setText(alimento.getNome());
        holder.textFoodDescription.setText(alimento.getDescricao());

        // Carrega a imagem usando Glide
        Glide.with(holder.itemView.getContext())
                .load(alimento.getImagemUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imageFood);

        // Verifica se o alimento já foi adicionado e altera o estado visual
        if (sharedViewModel.isAlimentoAdicionado(alimento)) {
            // Alimento já adicionado: desabilita o clique e altera o estado visual
            holder.itemView.setAlpha(0.5f); // Deixa o item mais "apagado"
            holder.itemView.setOnClickListener(null); // Desativa o clique
        } else {
            // Alimento ainda não foi adicionado: ativa o clique normalmente
            holder.itemView.setAlpha(1.0f); // Volta o estado visual ao normal
            holder.itemView.setOnClickListener(v -> {
                // Adiciona o alimento à lancheira
                sharedViewModel.adicionarAlimento(alimento);

                // Notifica o listener que o alimento foi clicado (se necessário para outras ações)
                if (listener != null) {
                    listener.onAlimentoClick(alimento);
                }

                // Atualiza o item visualmente após a adição
                notifyItemChanged(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    // Método para atualizar a lista de alimentos e notificar o RecyclerView
    public void setAlimentos(List<Alimentos> alimentos) {
        this.alimentos = alimentos;
        notifyDataSetChanged();
    }

    // ViewHolder interno que representa cada item de alimento
    public static class AlimentoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFood;
        TextView textFoodName;
        TextView textFoodDescription;

        public AlimentoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.image_food);
            textFoodName = itemView.findViewById(R.id.text_food_name);
            textFoodDescription = itemView.findViewById(R.id.text_food_description);
        }
    }
}