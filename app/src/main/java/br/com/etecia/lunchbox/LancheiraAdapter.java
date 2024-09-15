package br.com.etecia.lunchbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LancheiraAdapter extends RecyclerView.Adapter<LancheiraAdapter.LancheiraViewHolder> {

    private List<Alimentos> alimentosNaLancheira;

    public LancheiraAdapter(List<Alimentos> alimentosNaLancheira) {
        this.alimentosNaLancheira = alimentosNaLancheira;
    }

    @NonNull
    @Override
    public LancheiraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout de item da lancheira
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alimento, parent, false);
        return new LancheiraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LancheiraViewHolder holder, int position) {
        // Obter o alimento atual na lancheira
        Alimentos alimento = alimentosNaLancheira.get(position);

        // Definir os dados do alimento
        holder.textFoodName.setText(alimento.getName());
        holder.textFoodDescription.setText(alimento.getDescription());

        // Carrega a imagem usando Glide
        Glide.with(holder.itemView.getContext())
                .load(alimento.getImageUrl())
                .into(holder.imageFood);
    }

    @Override
    public int getItemCount() {
        // Retorna o número de alimentos na lancheira
        return alimentosNaLancheira.size();
    }

    // Método para remover um item específico da lista
    public void removerItem(int position) {
        if (position >= 0 && position < alimentosNaLancheira.size()) {
            alimentosNaLancheira.remove(position);
            notifyItemRemoved(position);  // Notificar a RecyclerView que o item foi removido
        }
    }

    static class LancheiraViewHolder extends RecyclerView.ViewHolder {

        TextView textFoodName;
        TextView textFoodDescription;
        ImageView imageFood;

        public LancheiraViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.text_food_name);
            textFoodDescription = itemView.findViewById(R.id.text_food_description);
            imageFood = itemView.findViewById(R.id.image_food);
        }
    }
}