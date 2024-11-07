package br.com.etecia.lunchbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class DiaAlimentosAdapter extends RecyclerView.Adapter<DiaAlimentosAdapter.AlimentosViewHolder> {

    private Context context;
    private List<Alimentos> alimentosList;

    public DiaAlimentosAdapter(Context context, List<Alimentos> alimentosList) {
        this.context = context;
        this.alimentosList = alimentosList;
    }

    @NonNull
    @Override
    public AlimentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_alimento, parent, false);
        return new AlimentosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentosViewHolder holder, int position) {
        Alimentos alimento = alimentosList.get(position);
        holder.nomeAlimentoTextView.setText(alimento.getNome());
        holder.descricaoTextView.setText(alimento.getDescricao());

        // Carregar a imagem do alimento com Glide (ou outro carregador de imagens)
        Glide.with(holder.itemView.getContext())
                .load(alimento.getImagemUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imagemAlimentoImageView);
    }

    @Override
    public int getItemCount() {
        return alimentosList.size();
    }

    public static class AlimentosViewHolder extends RecyclerView.ViewHolder {
        TextView nomeAlimentoTextView;
        TextView descricaoTextView;
        ImageView imagemAlimentoImageView;

        public AlimentosViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeAlimentoTextView = itemView.findViewById(R.id.text_food_name);
            descricaoTextView = itemView.findViewById(R.id.text_food_description);
            imagemAlimentoImageView = itemView.findViewById(R.id.image_food);
        }
    }
}