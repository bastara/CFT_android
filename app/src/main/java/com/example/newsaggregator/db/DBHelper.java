package com.example.newsaggregator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE1 = "news";
    public static final String TABLE2 = "tableOfSite";

    // имя БД
    public DBHelper(Context context) {
        super(context, "RSSDB11", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LOG_TAG = "ЛогКот";
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table " + TABLE1 + "("
                + "id integer primary key autoincrement,"
                + "title text,"
                + "link text,"
                + "description text,"
                + "category text,"
                + "isShow int DEFAULT \'0\',"
                //TODO нужно в дату превращать? есть рекомендация как стринг
                //TODO добавить поле просмотренная новость
                + "pubDate text" + ");");


//        db.execSQL("create table " + TABLE2 + "(" + "url_rss text primary key" + ");");
        db.execSQL("create table " + TABLE2 + "(" + "url_rss text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE1);
        db.execSQL("drop table if exists " + TABLE2);
        onCreate(db);
    }
}
