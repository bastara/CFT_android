package com.example.newsaggregator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsaggregator.data.db.DBHelper;

public class DBAdapter {

    private SQLiteDatabase dataBase;
    private Context context;

    public DBAdapter(Context context) {
        dataBase = new DBHelper(context).getWritableDatabase();
        this.context = context;
    }

    public SQLiteDatabase getDataBase() {
        return dataBase;
    }

    public void setDataBase(SQLiteDatabase dataBase) {
        this.dataBase = dataBase;
    }
}
