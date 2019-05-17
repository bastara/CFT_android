package com.example.newsaggregator.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.newsaggregator.data.Contract;

public class DBHelper extends SQLiteOpenHelper {

    DBHelper(Context context) {
        super(context, Contract.Entry.DATABASE_NAME, null, Contract.Entry.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LOG_TAG = "Лог!";
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table "
                + Contract.Entry.TABLE_NEWS + "("
                + Contract.Entry.COLUMN_ID + " INTEGER primary key autoincrement,"
                + Contract.Entry.COLUMN_TITLE + " text,"
                + Contract.Entry.COLUMN_LINK_NEWS + " text not null,"
                + Contract.Entry.COLUMN_URL + " text,"
                + Contract.Entry.COLUMN_DESCRIPTION + " text,"
                + Contract.Entry.COLUMN_CATEGORY + " text,"
                + Contract.Entry.COLUMN_IS_SHOW + " int DEFAULT \'0\',"
                //TODO нужно в дату превращать? есть рекомендация как стринг
                //TODO добавить поле просмотренная новость
                + Contract.Entry.COLUMN_PUB_DATE + " text" + ");");

        db.execSQL("create table " + Contract.Entry.TABLE_SITES + "("
//                + Contract.Entry.COLUMN_ID_SITE + " INTEGER primary key autoincrement,"
                + Contract.Entry.COLUMN_URL + " text primary key,"
                + Contract.Entry.COLUMN_TYPE_RESOURCE + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Contract.Entry.TABLE_NEWS);
        db.execSQL("drop table if exists " + Contract.Entry.TABLE_SITES);
        onCreate(db);
    }
}
