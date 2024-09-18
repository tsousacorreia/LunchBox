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

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lunchbox.db";
    private static final int DATABASE_VERSION = 3; // Atualize a versão do banco de dados

    // Tabela de perfis
    private static final String TABLE_PERFIS = "tbPerfis";
    private static final String COL_ID = "id";
    private static final String COL_NOME = "nome";
    private static final String COL_IDADE = "idade";
    private static final String COL_PREFERENCIAS = "preferencias";

    // Tabela de lancheiras
    private static final String TABLE_LANCHEIRAS = "tbLancheiras";
    private static final String COL_NOME_LANCHEIRA = "nome"; // Nome da lancheira
    private static final String COL_PERFIL_ID = "perfil_id"; // ID do perfil
    private static final String COL_DATA_LANCHEIRA = "data"; // Data da lancheira

    // Tabela de alimentos da lancheira
    private static final String TABLE_LANCHEIRA_ALIMENTOS = "tbLancheiraAlimentos";
    private static final String COL_LANCHEIRA_ID = "lancheira_id";
    private static final String COL_ALIMENTO_ID = "alimento_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela de perfis
        String createTablePerfis = "CREATE TABLE " + TABLE_PERFIS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME + " TEXT, " +
                COL_IDADE + " INTEGER, " +
                COL_PREFERENCIAS + " TEXT)";
        db.execSQL(createTablePerfis);

        // Criação da tabela de lancheiras
        String createTableLancheiras = "CREATE TABLE " + TABLE_LANCHEIRAS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID da lancheira
                COL_NOME_LANCHEIRA + " TEXT, " +
                COL_PERFIL_ID + " INTEGER, " +
                COL_DATA_LANCHEIRA + " TEXT, " +
                "FOREIGN KEY (" + COL_PERFIL_ID + ") REFERENCES " + TABLE_PERFIS + "(" + COL_ID + "))";
        db.execSQL(createTableLancheiras);

        // Criação da tabela de alimentos da lancheira
        String createTableLancheiraAlimentos = "CREATE TABLE " + TABLE_LANCHEIRA_ALIMENTOS + " (" +
                COL_LANCHEIRA_ID + " INTEGER, " +
                COL_ALIMENTO_ID + " INTEGER, " +
                "FOREIGN KEY (" + COL_LANCHEIRA_ID + ") REFERENCES " + TABLE_LANCHEIRAS + "(" + COL_ID + "), " +
                "PRIMARY KEY (" + COL_LANCHEIRA_ID + ", " + COL_ALIMENTO_ID + "))";
        db.execSQL(createTableLancheiraAlimentos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANCHEIRAS);
        onCreate(db);
    }

    // Método para adicionar perfil
    public long addPerfil(String nome, int idade, String preferencias) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME, nome);
        values.put(COL_IDADE, idade);
        values.put(COL_PREFERENCIAS, preferencias);

        return db.insert(TABLE_PERFIS, null, values);
    }

    // Método para recuperar perfis
    public List<Perfil> getPerfis() {
        List<Perfil> perfis = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERFIS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COL_ID);
            int nomeIndex = cursor.getColumnIndex(COL_NOME);
            int idadeIndex = cursor.getColumnIndex(COL_IDADE);
            int preferenciasIndex = cursor.getColumnIndex(COL_PREFERENCIAS);

            do {
                if (idIndex >= 0 && nomeIndex >= 0 && idadeIndex >= 0 && preferenciasIndex >= 0) {
                    Perfil perfil = new Perfil(
                            cursor.getInt(idIndex),
                            cursor.getString(nomeIndex),
                            cursor.getInt(idadeIndex),
                            cursor.getString(preferenciasIndex)
                    );
                    perfis.add(perfil);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return perfis;
    }

    // Método para adicionar lancheira
    public long addLancheira(String nome, String data, int perfilId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME_LANCHEIRA, nome);
        values.put(COL_DATA_LANCHEIRA, data);
        values.put(COL_PERFIL_ID, perfilId);

        return db.insert(TABLE_LANCHEIRAS, null, values);
    }

    // Método para recuperar lancheira por data e perfil
    public Cursor getLancheira(String data, int perfilId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LANCHEIRAS, null, COL_DATA_LANCHEIRA + "=? AND " + COL_PERFIL_ID + "=?",
                new String[]{data, String.valueOf(perfilId)}, null, null, null);
    }

    // Método para adicionar alimento à lancheira
    public void addAlimentoNaLancheira(long lancheiraId, int alimentoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LANCHEIRA_ID, lancheiraId);
        values.put(COL_ALIMENTO_ID, alimentoId);
        db.insert(TABLE_LANCHEIRA_ALIMENTOS, null, values);
    }

    // Método para recuperar alimentos de uma lancheira
    public List<Integer> getAlimentosNaLancheira(long lancheiraId) {
        List<Integer> alimentos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LANCHEIRA_ALIMENTOS, new String[]{COL_ALIMENTO_ID},
                COL_LANCHEIRA_ID + "=?", new String[]{String.valueOf(lancheiraId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            int alimentoIdIndex = cursor.getColumnIndex(COL_ALIMENTO_ID);

            if (alimentoIdIndex >= 0) {
                do {
                    alimentos.add(cursor.getInt(alimentoIdIndex));
                } while (cursor.moveToNext());
            } else {
                // Log or handle the error if the column index is invalid
                Log.e("DatabaseHelper", "Column index for " + COL_ALIMENTO_ID + " is invalid");
            }
        }
        cursor.close();
        return alimentos;
    }
}