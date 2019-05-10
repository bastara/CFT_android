package com.example.newsaggregator.data.db;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public final class MySingleton extends Application {

    private SQLiteDatabase dataBase;
    private DBHelper dbHelper = new DBHelper(this);

    @Override
    public void onCreate() {
        super.onCreate();
        dataBase = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDatabase() {
        return dataBase;
    }

    public Cursor getCursorNews() {
        return dataBase.query(
                NewsContract.NewsEntry.TABLE_NEWS,
                null,
                null,
                null,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_ID + " DESC");
    }

    public Cursor getCursorAllItem() {
        return dataBase.query(NewsContract.NewsEntry.TABLE_SITES,
                null,
                null,
                null,
                null,
                null,
                NewsContract.NewsEntry.COLUMN_URL);
    }

    public Cursor getCursorCheckSite(String tmpStr) {
        return dataBase.query(NewsContract.NewsEntry.TABLE_NEWS,
                null,
                NewsContract.NewsEntry.COLUMN_LINK_NEWS + "=?",
                new String[]{tmpStr},
                null,
                null,
                null);
    }

    public Cursor getCursorRefreshNews() {
        return dataBase.rawQuery("select * from " + NewsContract.NewsEntry.TABLE_SITES,
                null);
    }
}
