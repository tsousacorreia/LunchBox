package br.com.etecia.lunchbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaLancheiraAdapter extends RecyclerView.Adapter<DiaLancheiraAdapter.LancheiraViewHolder> {

    private List<Lancheira> lancheiras;
    private Context context;
    private String dataSelecionada;

    public DiaLancheiraAdapter(Context context, List<Lancheira> lancheiras, String dataSelecionada) {
        this.context = context;
        this.lancheiras = lancheiras;
        this.dataSelecionada = dataSelecionada;
    }

    @NonNull
    @Override
    public LancheiraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_lancheira, parent, false);
        return new LancheiraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LancheiraViewHolder holder, int position) {
        Lancheira lancheira = lancheiras.get(position);
        Perfil perfil = lancheira.getPerfil();

        // Exibir a data selecionada no TextView
        holder.diaTextView.setText(formatarDataParaExibicao(dataSelecionada));

        // Configura os dados do perfil (nome, idade, etc.)
        holder.nomePerfilTextView.setText(String.format("Lancheira: %s",perfil.getNome()));
        holder.idadeTextView.setText(String.format("Idade: %d anos", perfil.getIdade()));
        holder.preferenciasTextView.setText(String.format("Preferências: %s", perfil.getPreferencias()));

        // Configura o RecyclerView para exibir alimentos da lancheira
        DiaAlimentosAdapter alimentosAdapter = new DiaAlimentosAdapter(context, lancheira.getAlimentos());
        holder.alimentosRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.alimentosRecyclerView.setAdapter(alimentosAdapter);
    }

    @Override
    public int getItemCount() {
        return lancheiras.size();
    }

    // Método para formatar a data antes de exibir
    private String formatarDataParaExibicao(String data) {
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdfEntrada.parse(data);
            return sdfSaida.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return data;
        }
    }

    public static class LancheiraViewHolder extends RecyclerView.ViewHolder {
        TextView nomePerfilTextView;
        TextView idadeTextView;
        TextView preferenciasTextView;
        RecyclerView alimentosRecyclerView;
        TextView diaTextView;  // Adicionando TextView para mostrar a data

        public LancheiraViewHolder(@NonNull View itemView) {
            super(itemView);
            nomePerfilTextView = itemView.findViewById(R.id.text_profile_name);
            idadeTextView = itemView.findViewById(R.id.text_profile_age);
            preferenciasTextView = itemView.findViewById(R.id.text_profile_preferences);
            alimentosRecyclerView = itemView.findViewById(R.id.recycler_view_alimentos);
            diaTextView = itemView.findViewById(R.id.diaTextView);
        }
    }
}