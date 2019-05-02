package com.example.newsaggregator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RSSDB11";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LOG_TAG = "ЛогКот";
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table "
                + NewsContract.NewsEntry.TABLE_NAME1 + "("
                + NewsContract.NewsEntry.COLUMN_ID + " primary key autoincrement,"
                + NewsContract.NewsEntry.COLUMN_TITLE + " text,"
                + NewsContract.NewsEntry.COLUMN_LINK1 + " text,"
                + NewsContract.NewsEntry.COLUMN_DESCRIPTION + " text,"
                + NewsContract.NewsEntry.COLUMN_CATEGORY + " text,"
                + NewsContract.NewsEntry.COLUMN_ISSHOW + " int DEFAULT \'0\',"
                //TODO нужно в дату превращать? есть рекомендация как стринг
                //TODO добавить поле просмотренная новость
                + NewsContract.NewsEntry.COLUMN_PUBDATE + " text" + ");");


//        db.execSQL("create table " + TABLE2 + "(" + "url_rss text primary key" + ");");
        db.execSQL("create table " + NewsContract.NewsEntry.TABLE_NAME2 + "("
                + NewsContract.NewsEntry.COLUMN_LINK2 + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + NewsContract.NewsEntry.TABLE_NAME1);
        db.execSQL("drop table if exists " + NewsContract.NewsEntry.TABLE_NAME2);
        onCreate(db);
    }
}
