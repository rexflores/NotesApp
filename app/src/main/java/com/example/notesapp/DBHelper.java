package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Notes");
        onCreate(db);
    }

    public boolean insertNoteData(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        long result = db.insert("Notes", null, contentValues);
        return result != -1;
    }

    public boolean updateNoteData(String id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        long result = db.update("Notes", contentValues, "id=?", new String[]{id});
        return result != -1;
    }

    public boolean deleteNoteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Notes", "id=?", new String[]{id});
        return result != -1;
    }

    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT id, title, content FROM Notes", null);
    }


    public Cursor getNoteById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Notes WHERE id=?", new String[]{id});
    }

}

