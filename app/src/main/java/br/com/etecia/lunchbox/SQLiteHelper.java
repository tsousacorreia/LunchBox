package br.com.etecia.lunchbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lunchbox.db";
    private static final int DATABASE_VERSION = 7;

    // Tabelas e colunas
    private static final String TABLE_PERFIS = "tbPerfil";
    private static final String TABLE_LANCHEIRAS = "tbLancheira";
    private static final String TABLE_ALIMENTOS = "tbAlimentos";

    // SQL para criar a tabela de perfis
    private static final String CREATE_TABLE_PERFIL = "CREATE TABLE " + TABLE_PERFIS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome TEXT, " +
            "idade INTEGER, " +
            "preferencias TEXT);";

    // SQL para criar a tabela de lancheiras
    private static final String CREATE_TABLE_LANCHEIRA = "CREATE TABLE " + TABLE_LANCHEIRAS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "perfil_id INTEGER, " +
            "data TEXT, " +
            "FOREIGN KEY(perfil_id) REFERENCES " + TABLE_PERFIS + "(id) ON DELETE CASCADE);";

    // SQL para criar a tabela de alimentos
    private static final String CREATE_TABLE_ALIMENTOS = "CREATE TABLE " + TABLE_ALIMENTOS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome TEXT, " +
            "descricao TEXT, " +
            "imagemUrl TEXT);"; // Coluna imagemUrl adicionada

    // SQL para criar a tabela de associação lancheira-alimentos
    private static final String CREATE_TABLE_LANCHEIRA_ALIMENTOS = "CREATE TABLE tbLancheiraAlimentos (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "lancheira_id INTEGER, " +
            "alimento_id INTEGER, " +
            "FOREIGN KEY(lancheira_id) REFERENCES " + TABLE_LANCHEIRAS + "(id) ON DELETE CASCADE, " +
            "FOREIGN KEY(alimento_id) REFERENCES " + TABLE_ALIMENTOS + "(id) ON DELETE CASCADE);";

    // Instância única do SQLiteHelper
    private static SQLiteHelper instance;

    public static synchronized SQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteHelper(context.getApplicationContext());
        }
        return instance;
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PERFIL);
        db.execSQL(CREATE_TABLE_LANCHEIRA);
        db.execSQL(CREATE_TABLE_ALIMENTOS);
        db.execSQL(CREATE_TABLE_LANCHEIRA_ALIMENTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANCHEIRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALIMENTOS);
        db.execSQL("DROP TABLE IF EXISTS tbLancheiraAlimentos");
        onCreate(db);
    }

    public long inserirAlimento(Alimentos alimento) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar se o alimento já existe
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_ALIMENTOS + " WHERE nome = ?", new String[]{alimento.getNome()});
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex("id");
            if (idColumnIndex != -1) {
                long id = cursor.getLong(idColumnIndex);
                cursor.close();
                return id; // Retorna o ID existente se o alimento já está no banco
            }
        }
        cursor.close();

        // Inserir novo alimento se não existir
        ContentValues values = new ContentValues();
        values.put("nome", alimento.getNome());
        values.put("descricao", alimento.getDescricao());
        values.put("imagemUrl", alimento.getImagemUrl());
        return db.insert(TABLE_ALIMENTOS, null, values);
    }

    public long inserirLancheira(int perfilId, List<Alimentos> alimentos, String data) {
        if (data == null || perfilId <= 0) return -1;

        String dataFormatada = formatarDataParaBanco(data);
        if (dataFormatada == null) return -1;

        long lancheiraId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("perfil_id", perfilId);
            values.put("data", dataFormatada);
            lancheiraId = db.insert(TABLE_LANCHEIRAS, null, values);

            for (Alimentos alimento : alimentos) {
                long alimentoId = inserirAlimento(alimento);
                ContentValues alimentoValues = new ContentValues();
                alimentoValues.put("lancheira_id", lancheiraId);
                alimentoValues.put("alimento_id", alimentoId);
                db.insert("tbLancheiraAlimentos", null, alimentoValues);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("SQLiteHelper", "Erro ao inserir lancheira: " + e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        return lancheiraId;
    }

    public List<Lancheira> obterLancheirasPorData(String data) {
        List<Lancheira> lancheiras = new ArrayList<>();
        String dataFormatada = formatarDataParaBanco(data);

        if (dataFormatada == null) {
            Log.e("SQLiteHelper", "Data formatada é nula.");
            return lancheiras;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String queryLancheiras = "SELECT l.id AS lancheiraId, l.data, l.perfil_id, p.nome AS perfilNome, " +
                "p.idade AS perfilIdade, p.preferencias AS perfilPreferencias " +
                "FROM " + TABLE_LANCHEIRAS + " l " +
                "JOIN " + TABLE_PERFIS + " p ON l.perfil_id = p.id " +
                "WHERE l.data = ?";

        try (Cursor cursor = db.rawQuery(queryLancheiras, new String[]{dataFormatada})) {
            while (cursor.moveToNext()) {
                int lancheiraId = cursor.getInt(cursor.getColumnIndexOrThrow("lancheiraId"));
                int perfilId = cursor.getInt(cursor.getColumnIndexOrThrow("perfil_id"));
                String perfilNome = cursor.getString(cursor.getColumnIndexOrThrow("perfilNome"));
                int perfilIdade = cursor.getInt(cursor.getColumnIndexOrThrow("perfilIdade"));
                String perfilPreferencias = cursor.getString(cursor.getColumnIndexOrThrow("perfilPreferencias"));

                Perfil perfil = new Perfil(perfilId, perfilNome, perfilIdade, perfilPreferencias);
                List<Alimentos> alimentos = obterAlimentosPorLancheiraId(lancheiraId);
                lancheiras.add(new Lancheira(lancheiraId, perfilNome, data, alimentos, perfil));
            }
        }
        return lancheiras;
    }

    // Método para formatar a data para o padrão yyyy-MM-dd
    private String formatarDataParaBanco(String data) {
        try {
            // Remove o nome do dia, caso exista, assumindo que ele é seguido por uma vírgula e espaço
            if (data.contains(",")) {
                data = data.split(",")[1].trim();
            }

            SimpleDateFormat sdfEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfSaida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdfEntrada.parse(data);
            return sdfSaida.format(date);
        } catch (ParseException e) {
            Log.e("SQLiteHelper", "Erro ao formatar data: " + e.getMessage(), e);
            return null;
        }
    }


    // Método auxiliar para obter os alimentos da lancheira
    private List<Alimentos> obterAlimentosPorLancheiraId(int lancheiraId) {
        List<Alimentos> alimentos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String alimentosQuery = "SELECT a.id AS alimentoId, a.nome AS alimentoNome, a.descricao AS alimentoDescricao, a.imagemUrl " +
                "FROM " + TABLE_ALIMENTOS + " a " +
                "JOIN tbLancheiraAlimentos la ON a.id = la.alimento_id " +
                "WHERE la.lancheira_id = ?";
        Cursor cursor = db.rawQuery(alimentosQuery, new String[]{String.valueOf(lancheiraId)});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("alimentoId"));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("alimentoNome"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("alimentoDescricao"));
            String imagemUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagemUrl"));

            Alimentos alimento = new Alimentos(id, nome, descricao, imagemUrl);
            alimentos.add(alimento);
        }
        cursor.close();

        return alimentos;
    }
}