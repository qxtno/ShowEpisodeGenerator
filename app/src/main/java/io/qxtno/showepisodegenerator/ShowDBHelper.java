package io.qxtno.showepisodegenerator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.qxtno.showepisodegenerator.ShowContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "showlist.db";
    public static final int DATABASE_VERSION = 1;

    public ShowDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SHOWLIST_TABLE = "CREATE TABLE " + ShowEntry.TABLE_NAME + " (" +
                ShowEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

    public void addShow(Show show) {
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ShowEntry.COLUMN_TITLE, show.getTitle());
        contentValues.put(ShowEntry.COLUMN_SEASONS, Arrays.toString(show.getSeasons()));
        contentValues.put(ShowEntry.COLUMN_FAV, show.getFav());

        mDatabase.insert(ShowEntry.TABLE_NAME, null, contentValues);
        mDatabase.close();
    }

    public Show getShow(int id) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(ShowEntry.TABLE_NAME, new String[]{ShowEntry.COLUMN_ID, ShowEntry.COLUMN_TITLE, ShowEntry.COLUMN_SEASONS, ShowEntry.COLUMN_FAV}, ShowEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        assert cursor != null;
        String seasonString = cursor.getString(2);
        String[] items = seasonString.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", "").split(",");
        int[] seasons = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                seasons[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                Log.i("To Array Error DBHelper", "Error while parsing string into an array");
            }
        }
        return new Show(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), seasons, Integer.parseInt(cursor.getString(3)));
    }

    public int updateShow(Show show){
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ShowEntry.COLUMN_FAV, show.getFav());

        return mDatabase.update(ShowEntry.TABLE_NAME,contentValues, ShowEntry.COLUMN_ID + " = ?",
                new String[] {String.valueOf(show.getId())});
    }

    public List<Show> getAllShows(){
        List<Show> showList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ShowEntry.TABLE_NAME;

        SQLiteDatabase mDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = mDatabase.rawQuery(selectQuery,null);
        if(cursor.moveToNext()){
            do{
                Show show = new Show();
                show.setId(Integer.parseInt(cursor.getString(0)));
                show.setTitle(cursor.getString(1));
                String seasonString = cursor.getString(2);
                String[] items = seasonString.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", "").split(",");
                int[] seasons = new int[items.length];

                for (int i = 0; i < items.length; i++) {
                    try {
                        seasons[i] = Integer.parseInt(items[i]);
                    } catch (NumberFormatException nfe) {
                        Log.i("To Array Error DBHelper", "Error while parsing string into an array");
                    }
                }
                show.setSeasons(seasons);
                show.setFav(Integer.parseInt(cursor.getString(3)));
                showList.add(show);
            }while (cursor.moveToNext());
        }
        return showList;
    }

}
