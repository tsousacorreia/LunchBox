package br.com.etecia.lunchbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lunchbox.db";
    private static final int DATABASE_VERSION = 2;

    // Tabelas e colunas
    private static final String TABLE_PERFIS = "tbPerfil";
    private static final String TABLE_LANCHEIRAS = "tbLancheira";
    private static final String COLUMN_LANCHEIRA_ID = "id";
    private static final String COLUMN_PERFIL_ID = "perfil_id";
    private static final String COLUMN_DATA = "data";
    private static final String TABLE_LANCHEIRA_ALIMENTOS = "tbLancheiraAlimentos";
    private static final String COLUMN_ALIMENTO_ID = "alimento_id";
    private static final String COLUMN_LANCHEIRA_ID_FK = "lancheira_id";

    // SQL para criar a tabela de perfis
    private static final String CREATE_TABLE_PERFIL = "CREATE TABLE " + TABLE_PERFIS + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nome TEXT, "
            + "idade INTEGER, "
            + "preferencias TEXT);";

    // SQL para criar a tabela de lancheiras
    private static final String CREATE_TABLE_LANCHEIRA = "CREATE TABLE " + TABLE_LANCHEIRAS + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "perfil_id INTEGER, "
            + "data TEXT, "
            + "FOREIGN KEY(perfil_id) REFERENCES " + TABLE_PERFIS + "(id) ON DELETE CASCADE);";

    // SQL para criar a tabela de lancheira-alimentos
    private static final String CREATE_TABLE_LANCHEIRA_ALIMENTOS = "CREATE TABLE " + TABLE_LANCHEIRA_ALIMENTOS + " ("
            + COLUMN_LANCHEIRA_ID_FK + " INTEGER, "
            + COLUMN_ALIMENTO_ID + " INTEGER, "
            + "PRIMARY KEY(" + COLUMN_LANCHEIRA_ID_FK + ", " + COLUMN_ALIMENTO_ID + "), "
            + "FOREIGN KEY(" + COLUMN_LANCHEIRA_ID_FK + ") REFERENCES " + TABLE_LANCHEIRAS + "(" + COLUMN_LANCHEIRA_ID + ") ON DELETE CASCADE);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação das tabelas de perfis, lancheiras e lancheira-alimentos
        db.execSQL(CREATE_TABLE_PERFIL);
        db.execSQL(CREATE_TABLE_LANCHEIRA);
        db.execSQL(CREATE_TABLE_LANCHEIRA_ALIMENTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualizar o esquema do banco de dados, se necessário
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANCHEIRA_ALIMENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANCHEIRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFIS);
        onCreate(db);
    }

    // Inserir uma nova lancheira no banco de dados
    public long inserirLancheira(int perfilId, List<Alimentos> alimentos, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        long lancheiraId = -1;

        try {
            // Inicia uma transação
            db.beginTransaction();

            // Insere a lancheira e obtém o ID
            ContentValues lancheiraValues = new ContentValues();
            lancheiraValues.put(COLUMN_PERFIL_ID, perfilId);
            lancheiraValues.put(COLUMN_DATA, data);
            lancheiraId = db.insert(TABLE_LANCHEIRAS, null, lancheiraValues);

            if (lancheiraId != -1) {
                // Insere os alimentos associados à lancheira
                for (Alimentos alimento : alimentos) {
                    ContentValues alimentoValues = new ContentValues();
                    alimentoValues.put(COLUMN_LANCHEIRA_ID_FK, lancheiraId);
                    alimentoValues.put(COLUMN_ALIMENTO_ID, alimento.getId());
                    db.insert(TABLE_LANCHEIRA_ALIMENTOS, null, alimentoValues);
                }
            }

            // Marca a transação como bem-sucedida
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fecha a transação
            db.endTransaction();
        }

        return lancheiraId;
    }

    // Método para buscar lancheiras por data
    public Cursor buscarLancheirasPorData(String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_LANCHEIRAS + " WHERE " + COLUMN_DATA + " = ?";
        return db.rawQuery(query, new String[]{data});
    }
}