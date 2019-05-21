package com.example.newsaggregator;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsaggregator.data.db.DBRequest;

public final class MyApplication extends Application {

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
