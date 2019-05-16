package com.example.newsaggregator.data.db;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public final class DBAdapter extends Application {

//    private SQLiteDatabase dataBase;
//    private DBHelper dbHelper = new DBHelper(this);
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        dataBase = dbHelper.getWritableDatabase();
//    }
//
//    public SQLiteDatabase getDatabase() {
//        return dataBase;
//    }

    public DBRequest dbRequest;

    @Override
    public void onCreate() {
        super.onCreate();
        dbRequest = new DBRequest(this);
    }

    public SQLiteDatabase getDatabase() {
        return dbRequest.dataBase;
    }
}
