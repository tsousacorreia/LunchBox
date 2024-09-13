package br.com.etecia.lunchbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LancheiraAdapter extends RecyclerView.Adapter<LancheiraAdapter.LancheiraViewHolder> {

    private List<FoodItem> alimentosNaLancheira;

    public LancheiraAdapter(List<FoodItem> alimentosNaLancheira) {
        this.alimentosNaLancheira = alimentosNaLancheira;
    }

    @NonNull
    @Override
    public LancheiraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alimento, parent, false);
        return new LancheiraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LancheiraViewHolder holder, int position) {
        FoodItem alimento = alimentosNaLancheira.get(position);
        holder.nomeAlimento.setText(alimento.getName());
        holder.descricaoAlimento.setText(alimento.getDescription());

        // Aqui você pode carregar a imagem, se houver uma URL
        // Por exemplo: Picasso.get().load(alimento.getImagemUrl()).into(holder.imagemAlimento);
    }

    @Override
    public int getItemCount() {
        return alimentosNaLancheira.size();
    }

    static class LancheiraViewHolder extends RecyclerView.ViewHolder {

        TextView nomeAlimento;
        TextView descricaoAlimento;
        ImageView imagemAlimento;

        public LancheiraViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeAlimento = itemView.findViewById(R.id.text_food_name);
            descricaoAlimento = itemView.findViewById(R.id.text_food_description);
            imagemAlimento = itemView.findViewById(R.id.image_food);
        }
    }
}