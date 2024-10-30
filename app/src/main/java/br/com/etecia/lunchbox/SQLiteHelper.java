package br.com.etecia.lunchbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lunchbox.db";
    private static final int DATABASE_VERSION = 5;

    // Tabelas e colunas
    private static final String TABLE_PERFIS = "tbPerfil";
    private static final String TABLE_LANCHEIRAS = "tbLancheira";
    private static final String TABLE_ALIMENTOS = "tbAlimentos";
    private static final String COLUMN_ALIMENTO_ID = "alimento_id";
    private static final String COLUMN_LANCHEIRA_ID_FK = "lancheira_id";

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

    // SQL para criar a tabela de alimentos com a coluna imagemUrl
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
        // Atualiza as tabelas se a versão do banco de dados aumentar
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANCHEIRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALIMENTOS);
        db.execSQL("DROP TABLE IF EXISTS tbLancheiraAlimentos");
        onCreate(db);
    }

    public long inserirAlimento(Alimentos alimento) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar se o alimento já existe
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_ALIMENTOS + " WHERE nome = ?", new String[]{alimento.getNome()});
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex("id");
            if (idColumnIndex != -1) {
                id = cursor.getInt(idColumnIndex);
            } else {
                Log.e("SQLiteHelper", "Coluna 'id' não encontrada na tabela " + TABLE_ALIMENTOS);
            }
        }
        cursor.close();

        // Inserir novo alimento se não existir
        ContentValues values = new ContentValues();
        values.put("nome", alimento.getNome());
        values.put("descricao", alimento.getDescricao());
        values.put("imagemUrl", alimento.getImagemUrl());
        id = db.insert(TABLE_ALIMENTOS, null, values);

        return id; // Retorna o ID do novo alimento
    }

    public long inserirLancheira(int perfilId, List<Alimentos> alimentos, String data) {
        long lancheiraId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("perfil_id", perfilId);
            values.put("data", data);
            lancheiraId = db.insert(TABLE_LANCHEIRAS, null, values);

            for (Alimentos alimento : alimentos) {
                long alimentoId = inserirAlimento(alimento); // Insere o alimento e obtém o ID
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

    public List<Lancheira> obterLancheirasPorPerfil(int perfilId) {
        List<Lancheira> lancheiras = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LANCHEIRAS + " WHERE perfil_id = ?", new String[]{String.valueOf(perfilId)});

        if (cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex("id");
                int dataColumnIndex = cursor.getColumnIndex("data");

                if (idColumnIndex != -1 && dataColumnIndex != -1) {
                    int lancheiraId = cursor.getInt(idColumnIndex);
                    String data = cursor.getString(dataColumnIndex);

                    List<Alimentos> alimentos = obterAlimentosPorLancheira(lancheiraId);

                    Lancheira lancheira = new Lancheira(
                            lancheiraId,
                            "Nome da Lancheira",
                            data,
                            alimentos,
                            perfilId
                    );
                    lancheiras.add(lancheira);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lancheiras;
    }

    public List<Alimentos> obterAlimentosPorLancheira(int lancheiraId) {
        List<Alimentos> alimentos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT a.id, a.nome, a.descricao, a.imagemUrl FROM " + TABLE_ALIMENTOS + " a " +
                "INNER JOIN tbLancheiraAlimentos la ON a.id = la.alimento_id " +
                "WHERE la.lancheira_id = ?", new String[]{String.valueOf(lancheiraId)});

        if (cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nomeColumnIndex = cursor.getColumnIndex("nome");
                int descricaoColumnIndex = cursor.getColumnIndex("descricao");
                int imagemUrlColumnIndex = cursor.getColumnIndex("imagemUrl");

                if (idColumnIndex != -1 && nomeColumnIndex != -1 && descricaoColumnIndex != -1 && imagemUrlColumnIndex != -1) {
                    Alimentos alimento = new Alimentos(
                            cursor.getInt(idColumnIndex),
                            cursor.getString(nomeColumnIndex),
                            cursor.getString(descricaoColumnIndex),
                            cursor.getString(imagemUrlColumnIndex)
                    );
                    alimentos.add(alimento);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alimentos;
    }
}