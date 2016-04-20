package com.dissoi.guiaapp2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class GuiaAppDatabaseHelper extends SQLiteAssetHelper
{
    private static final String DATABASE_NAME = "GuiaApp.db";
    private static final int DATABASE_VERSION = 1;

    public GuiaAppDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addFavorito(int id_empresa)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor favoritos = getFavorito(id_empresa);

        if (favoritos.getCount() == 0)
        {
            ContentValues values = new ContentValues();
            values.put("id_empresa", id_empresa);

            db.insert("favorito", null, values);
        }
    }

    public void removeFavorito(int id_empresa)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor favoritos = getFavorito(id_empresa);

        if (favoritos.getCount() > 0)
        {
            String selection = " id_empresa = ?";
            String[] selectionArgs = {String.valueOf(id_empresa)};

            db.delete("favorito", selection, selectionArgs);
        }
    }

    public Cursor getFavorito(int id_empresa)
    {
        SQLiteDatabase db = getReadableDatabase();

        String [] sqlSelect = {"id", "id_empresa"};
        String sqlTables = "favorito";
        String selection = " id_empresa = ?";
        String [] selectionArgs = {String.valueOf(id_empresa)};

        Cursor c = db.query(sqlTables, sqlSelect, selection, selectionArgs, null, null, null);

        c.moveToFirst();
        return c;
    }

    public Cursor getFavoritos()
    {
        SQLiteDatabase db = getReadableDatabase();

        String [] sqlSelect = {"id", "id_empresa"};
        String sqlTables = "favorito";

        Cursor c = db.query(sqlTables, sqlSelect, null, null, null, null, null);

        //c.moveToFirst();
        return c;
    }
}