package com.example.newsaggregator.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsaggregator.data.Contract;

public class DBRequest {
    public SQLiteDatabase dataBase;

    public DBRequest(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        dataBase = dbHelper.getWritableDatabase();
    }

    public Cursor getCursorNews() {
        return dataBase.query(
                Contract.Entry.TABLE_NEWS,
                null,
                null,
                null,
                null,
                null,
                Contract.Entry.COLUMN_ID + " DESC");
    }

    public Cursor getCursorAllItem() {
        return dataBase.query(Contract.Entry.TABLE_SITES,
                null,
                null,
                null,
                null,
                null,
                Contract.Entry.COLUMN_URL);
    }

    public Cursor getCursorCheckSite(String tmpStr) {
        return dataBase.query(Contract.Entry.TABLE_NEWS,
                null,
                Contract.Entry.COLUMN_LINK_NEWS + "=?",
                new String[]{tmpStr},
                null,
                null,
                null);
    }

    public Cursor getCursorRefreshNews() {
        return dataBase.rawQuery("select * from " + Contract.Entry.TABLE_SITES,
                null);
    }
}
