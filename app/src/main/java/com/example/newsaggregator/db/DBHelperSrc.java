package com.example.newsaggregator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperSrc extends SQLiteOpenHelper {

    public static final String TABLE = "tableOfSite";


    public DBHelperSrc(Context context) {
        super(context, "RSSDB2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LOG_TAG = "rssDB";
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table " + TABLE + "(" + "url_rss text primary key" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE);
        onCreate(db);
    }
}
