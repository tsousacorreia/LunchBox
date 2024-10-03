package br.com.etecia.lunchbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PerfilDAO {

    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;

    public PerfilDAO(Context context) {
        dbHelper = new SQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Inserir um perfil no banco de dados
    public long inserirPerfil(Perfil perfil) {
        ContentValues values = new ContentValues();
        values.put("nome", perfil.getNome());
        values.put("idade", perfil.getIdade());
        values.put("preferencias", perfil.getPreferencias());
        return database.insert("tbPerfil", null, values);
    }

    // Listar todos os perfis
    public List<Perfil> listarPerfis() {
        List<Perfil> perfis = new ArrayList<>();
        Cursor cursor = database.query("tbPerfil",
                new String[]{"id", "nome", "idade", "preferencias"},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                int idade = cursor.getInt(cursor.getColumnIndexOrThrow("idade"));
                String preferencias = cursor.getString(cursor.getColumnIndexOrThrow("preferencias"));

                // Criação do objeto Perfil, chamando o construtor correto
                Perfil perfil = new Perfil(nome, idade, preferencias);
                perfil.setId(id); // Define o ID após a criação do objeto
                perfis.add(perfil);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return perfis;
    }

    // Atualizar um perfil
    public int atualizarPerfil(Perfil perfil) {
        ContentValues values = new ContentValues();
        values.put("nome", perfil.getNome());
        values.put("idade", perfil.getIdade());
        values.put("preferencias", perfil.getPreferencias());

        return database.update("tbPerfil", values, "id = ?", new String[]{String.valueOf(perfil.getId())});
    }

    // Deletar um perfil
    public int deletarPerfil(int perfilId) {
        return database.delete("tbPerfil", "id = ?", new String[]{String.valueOf(perfilId)});
    }
}