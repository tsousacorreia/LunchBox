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

    private List<FoodItem> alimentos = new ArrayList<>();
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
        FoodItem alimento = alimentos.get(position);
        holder.textFoodName.setText(alimento.getName());
        holder.textFoodDescription.setText(alimento.getDescription());
        // Supondo que a URL da imagem está em alimento.getImageUrl()
        Glide.with(holder.itemView.getContext())
                .load(alimento.getImageUrl())
                .into(holder.imageFood);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlimentoClick(alimento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    public void setAlimentos(List<FoodItem> alimentos) {
        this.alimentos = alimentos;
        notifyDataSetChanged();
    }

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