package com.example.newsaggregator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String NEWS_TABLE = "newsss";


    public DBHelper(Context context) {
        super(context, "newsBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LOG_TAG = "ЛогКот";
//        db.execSQL("drop table if exists " + NEWS_TABLE);
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table " + NEWS_TABLE + "("
                + "id integer primary key autoincrement,"
                + "title text,"
                + "link text,"
                + "description text,"
                + "category text,"
                //TODO нужно в дату превращать? есть рекомендация как стринг
                + "pubDate text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + NEWS_TABLE);
        onCreate(db);
    }
}
