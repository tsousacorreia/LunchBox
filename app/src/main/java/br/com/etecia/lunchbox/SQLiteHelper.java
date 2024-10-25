package br.com.etecia.lunchbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lunchbox.db";
    private static final int DATABASE_VERSION = 3;

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

    // SQL para criar a tabela de alimentos
    private static final String CREATE_TABLE_ALIMENTOS = "CREATE TABLE " + TABLE_ALIMENTOS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome TEXT, " +
            "descricao TEXT);";

    // SQL para criar a tabela de lancheira-alimentos (associação)
    private static final String CREATE_TABLE_LANCHEIRA_ALIMENTO = "CREATE TABLE " + "tbLancheiraAlimento" + " (" +
            COLUMN_LANCHEIRA_ID_FK + " INTEGER, " +
            COLUMN_ALIMENTO_ID + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_LANCHEIRA_ID_FK + ") REFERENCES " + TABLE_LANCHEIRAS + "(id), " +
            "FOREIGN KEY(" + COLUMN_ALIMENTO_ID + ") REFERENCES " + TABLE_ALIMENTOS + "(id));";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PERFIL);
        db.execSQL(CREATE_TABLE_LANCHEIRA);
        db.execSQL(CREATE_TABLE_ALIMENTOS);
        db.execSQL(CREATE_TABLE_LANCHEIRA_ALIMENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANCHEIRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALIMENTOS);
        onCreate(db);
    }

    public long inserirLancheira(int perfilId, List<Alimentos> alimentos, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        long lancheiraId;
        try {
            db.beginTransaction();
            // Inserindo a lancheira
            ContentValues values = new ContentValues();
            values.put("perfil_id", perfilId);
            values.put("data", data);
            lancheiraId = db.insert(TABLE_LANCHEIRAS, null, values);

            // Inserindo os alimentos associados à lancheira
            for (Alimentos alimento : alimentos) {
                ContentValues alimentoValues = new ContentValues();
                alimentoValues.put(COLUMN_LANCHEIRA_ID_FK, lancheiraId);
                alimentoValues.put(COLUMN_ALIMENTO_ID, alimento.getId());
                db.insert("tbLancheiraAlimento", null, alimentoValues);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            lancheiraId = -1; // Retorna -1 em caso de erro
        } finally {
            db.endTransaction();
        }
        return lancheiraId;
    }

    public List<Alimentos> buscarLancheiraPorData(String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Alimentos> alimentosList = new ArrayList<>();
        String query = "SELECT a.* FROM tbAlimentos a " +
                "JOIN tbLancheiraAlimento la ON a.id = la.alimento_id " +
                "JOIN tbLancheira l ON l.id = la.lancheira_id " +
                "WHERE l.data = ?";
        Cursor cursor = db.rawQuery(query, new String[]{data});

        if (cursor.moveToFirst()) {
            do {
                Alimentos alimento = new Alimentos();
                alimento.setId(cursor.getInt(cursor.getColumnIndex("id")));
                alimento.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                alimento.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                alimentosList.add(alimento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alimentosList;
    }
}