package br.com.etecia.lunchbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LancheiraProntaAdapter extends RecyclerView.Adapter<LancheiraProntaAdapter.LancheiraViewHolder> {

    private List<Lancheira> lancheiras;

    public LancheiraProntaAdapter(List<Lancheira> lancheiras) {
        this.lancheiras = lancheiras;
    }

    @NonNull
    @Override
    public LancheiraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lancheira, parent, false);
        return new LancheiraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LancheiraViewHolder holder, int position) {
        Lancheira lancheira = lancheiras.get(position);
        holder.bind(lancheira); // Usando o método bind para configurar a view
    }

    @Override
    public int getItemCount() {
        return lancheiras.size();
    }

    public void setLancheiras(List<Lancheira> lancheiras) {
        this.lancheiras = lancheiras;
        notifyDataSetChanged();
    }

    class LancheiraViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNome;
        private TextView textViewData;
        private TextView textViewAlimentos;
        private TextView textViewPerfil;

        public LancheiraViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.text_view_nome_lancheira);
            textViewData = itemView.findViewById(R.id.text_view_data);
            textViewAlimentos = itemView.findViewById(R.id.text_view_alimentos);
            textViewPerfil = itemView.findViewById(R.id.text_view_perfil);
        }

        public void bind(Lancheira lancheira) {
            textViewNome.setText("Lancheira: " + lancheira.getNomeLancheira()); // Acessando o nome corretamente
            textViewData.setText("Data: " + lancheira.getData());

            // Exibir a lista de alimentos como uma String
            StringBuilder alimentosStringBuilder = new StringBuilder("Alimentos: ");
            for (Alimentos alimento : lancheira.getAlimentos()) {
                alimentosStringBuilder.append(alimento.getNome()).append(", ");
            }
            // Remove a última vírgula e espaço
            if (alimentosStringBuilder.length() > 11) {
                alimentosStringBuilder.setLength(alimentosStringBuilder.length() - 2);
            }
            textViewAlimentos.setText(alimentosStringBuilder.toString());

            // Obter o nome do perfil usando o perfilId
            String nomePerfil = obterNomePerfil(lancheira.getPerfilId()); // Chame o método para obter o nome do perfil
            textViewPerfil.setText("Perfil: " + nomePerfil);
        }

        private String obterNomePerfil(int perfilId) {
            // Lógica para buscar o nome do perfil no banco de dados usando o perfilId
            // Pode ser feito através do seu SQLiteHelper
            return "Nome do Perfil"; // Retorne o nome correspondente
        }
    }
}