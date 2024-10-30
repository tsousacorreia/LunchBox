package br.com.etecia.lunchbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiaAlimentosAdapter extends RecyclerView.Adapter<DiaAlimentosAdapter.AlimentoViewHolder> {

    private List<Alimentos> alimentosList;
    private Context context;

    public DiaAlimentosAdapter(List<Alimentos> alimentosList, Context context) {
        this.alimentosList = alimentosList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlimentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_alimento, parent, false);
        return new AlimentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentoViewHolder holder, int position) {
        Alimentos alimento = alimentosList.get(position);
        holder.nomeTextView.setText(alimento.getNome());
        holder.descricaoTextView.setText(alimento.getDescricao());
        // Aqui você pode usar uma biblioteca como Glide ou Picasso para carregar a imagem
        // Glide.with(context).load(alimento.getImagemUrl()).into(holder.imagemImageView);
    }

    @Override
    public int getItemCount() {
        return alimentosList.size();
    }

    static class AlimentoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;
        TextView descricaoTextView;
        ImageView imagemImageView;

        AlimentoViewHolder(View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.text_food_name);
            descricaoTextView = itemView.findViewById(R.id.text_food_description);
            imagemImageView = itemView.findViewById(R.id.image_food);
        }
    }
}