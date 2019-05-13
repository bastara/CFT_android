package com.example.newsaggregator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.data.db.DBAdapter;

public class DBRequest {
    private SQLiteDatabase dataBase;

    public DBRequest(Context context) {
        DBAdapter dbAdapter = (DBAdapter) context.getApplicationContext();
        dataBase = dbAdapter.getDatabase();
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
