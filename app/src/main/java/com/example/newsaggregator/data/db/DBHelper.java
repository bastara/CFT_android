package com.example.newsaggregator.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RSSDB22";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LOG_TAG = "Лог!";
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table "
                + NewsContract.NewsEntry.TABLE_NEWS + "("
                + NewsContract.NewsEntry.COLUMN_ID + " INTEGER primary key autoincrement,"
                + NewsContract.NewsEntry.COLUMN_TITLE + " text,"
                + NewsContract.NewsEntry.COLUMN_LINK_NEWS + " text not null,"
                + NewsContract.NewsEntry.COLUMN_URL + " text,"
                + NewsContract.NewsEntry.COLUMN_DESCRIPTION + " text,"
                + NewsContract.NewsEntry.COLUMN_CATEGORY + " text,"
                + NewsContract.NewsEntry.COLUMN_ISSHOW + " int DEFAULT \'0\',"
                //TODO нужно в дату превращать? есть рекомендация как стринг
                //TODO добавить поле просмотренная новость
                + NewsContract.NewsEntry.COLUMN_PUBDATE + " text" + ");");

        db.execSQL("create table " + NewsContract.NewsEntry.TABLE_SITES + "("
//                + NewsContract.NewsEntry.COLUMN_ID_SITE + " INTEGER primary key autoincrement,"
                + NewsContract.NewsEntry.COLUMN_URL + " text primary key" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + NewsContract.NewsEntry.TABLE_NEWS);
        db.execSQL("drop table if exists " + NewsContract.NewsEntry.TABLE_SITES);
        onCreate(db);
    }
}
