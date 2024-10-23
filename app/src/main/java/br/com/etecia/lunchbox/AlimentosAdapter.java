package br.com.etecia.lunchbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AlimentosAdapter extends RecyclerView.Adapter<AlimentosAdapter.AlimentoViewHolder> {

    private List<Alimentos> alimentos = new ArrayList<>();
    private SharedViewModel sharedViewModel;
    private PerfilViewModel perfilViewModel;
    private OnAlimentoClickListener listener;

    // Atualiza o construtor para incluir o PerfilViewModel
    public AlimentosAdapter(SharedViewModel sharedViewModel, PerfilViewModel perfilViewModel, OnAlimentoClickListener listener) {
        this.sharedViewModel = sharedViewModel;
        this.perfilViewModel = perfilViewModel;
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

        // Verifica se o alimento já está na lancheira
        boolean isAlimentoAdicionado = sharedViewModel.isAlimentoAdicionado(alimento);

        if (isAlimentoAdicionado) {
            // Se o alimento já está na lancheira, desabilita o clique e modifica o visual
            holder.itemView.setAlpha(0.5f); // Deixa o item mais "apagado"
            holder.itemView.setClickable(false); // Desativa o clique
        } else {
            // Se o alimento ainda não está na lancheira, habilita o clique e restaura o visual
            holder.itemView.setAlpha(1.0f); // Volta o estado visual ao normal
            holder.itemView.setClickable(true); // Habilita o clique
            holder.itemView.setOnClickListener(v -> {
                // Verifica se há um perfil selecionado antes de adicionar o alimento
                if (perfilViewModel.getPerfilSelecionado().getValue() != null) {
                    // Verifica novamente se o alimento já está na lancheira antes de adicionar
                    if (!sharedViewModel.isAlimentoAdicionado(alimento)) {
                        // Adiciona o alimento à lancheira no ViewModel
                        sharedViewModel.adicionarAlimento(alimento);

                        // Atualiza visualmente o item
                        holder.itemView.setAlpha(0.5f);
                        holder.itemView.setClickable(false); // Desativa o clique

                        // Notifica o listener (se necessário)
                        if (listener != null) {
                            listener.onAlimentoClick(alimento);
                        }
                    } else {
                        // Informa ao usuário que o alimento já está na lancheira
                        Toast.makeText(holder.itemView.getContext(), "Este alimento já está na lancheira.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Mostra uma mensagem de erro se nenhum perfil estiver selecionado
                    Toast.makeText(holder.itemView.getContext(), "Por favor, selecione um perfil antes de adicionar alimentos.", Toast.LENGTH_SHORT).show();
                }
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