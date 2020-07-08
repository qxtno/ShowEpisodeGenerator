package io.qxtno.showepisodegenerator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.qxtno.showepisodegenerator.ShowContract.*;

import androidx.annotation.Nullable;

public class ShowDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "showlist.db";
    public static final int DATABASE_VERSION = 1;

    public ShowDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SHOWLIST_TABLE = "CREATE TABLE " + ShowEntry.TABLE_NAME + " (" +
                ShowEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ShowEntry.COLUMN_TITLE + " TEXT NOT NULL, " + ShowEntry.COLUMN_SEASONS + " TEXT NOT NULL, " +
                ShowEntry.COLUMN_FAV + " TEXT NOT NULL, " + ShowEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_SHOWLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ShowEntry.TABLE_NAME);
        onCreate(db);
    }

    public Cursor selectList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+ShowEntry.TABLE_NAME;
        return db.rawQuery(sql,null,null);
    }

}
