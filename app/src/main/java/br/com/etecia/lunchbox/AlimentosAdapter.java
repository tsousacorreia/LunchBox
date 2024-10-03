package br.com.etecia.lunchbox;

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
    private OnAlimentoClickListener listener;

    public AlimentosAdapter(OnAlimentoClickListener listener) {
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

        // Define o clique no item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlimentoClick(alimento);  // Passa o item clicado para o listener
            }
        });
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