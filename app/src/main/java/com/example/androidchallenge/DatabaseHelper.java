package com.example.androidchallenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BBC.db";

    private static final String SQL_CREATE_FAVORITE =
            "CREATE TABLE " + FavoriteTable.TABLE_NAME + " (" +
                    FavoriteTable._ID + " INTEGER PRIMARY KEY," +
                    FavoriteTable.COLUMN_NAME_CHARACTER_ID + " TEXT)";

    private static final String SQL_DELETE_FAVORITE =
            "DROP TABLE IF EXISTS " + FavoriteTable.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVORITE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    private static class FavoriteTable implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME_CHARACTER_ID = "character_id";
    }

    public ArrayList<Integer> getFavoriteCharactersIDs(){
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FavoriteTable.COLUMN_NAME_CHARACTER_ID
        };

        String selection = FavoriteTable.COLUMN_NAME_CHARACTER_ID + " = ?";
        String[] selectionArgs = { "My Title" };

        String sortOrder =
                FavoriteTable.COLUMN_NAME_CHARACTER_ID + " ASC";

        Cursor cursor = db.query(
                FavoriteTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        ArrayList<Integer> characterIDs = new ArrayList<>();
        while(cursor.moveToNext()) {
            int characterID = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavoriteTable.COLUMN_NAME_CHARACTER_ID));
            characterIDs.add(characterID);
        }
        cursor.close();

        return characterIDs;
    }

    public void removeFavoriteCharacterID(int characterID){
        SQLiteDatabase db = getWritableDatabase();
        String selection = FavoriteTable.COLUMN_NAME_CHARACTER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(characterID) };
        int deletedRows = db.delete(FavoriteTable.TABLE_NAME, selection, selectionArgs);
    }

    public void addFavoriteCharacterID(int characterID){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteTable.COLUMN_NAME_CHARACTER_ID, characterID);
        long newRowId = db.insert(FavoriteTable.TABLE_NAME, null, values);
    }
}


