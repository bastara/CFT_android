package com.example.newsaggregator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddPage {
    public static void addPage(String src, Context context) {
        final String TAG = "rssDB";
        DBHelperSrc dbHelperSrc = new DBHelperSrc(context);

        SQLiteDatabase database = dbHelperSrc.getWritableDatabase();

        ContentValues cv = new ContentValues();

        try {
            Log.d(TAG, "Добавляю в источники RSS " + src);
            cv.put("url_rss", src);

            Log.d(TAG, "Вношу данные БД ");
            long rowID = database.insert("site_table", null, cv);
            Log.d(TAG, "НОМЕР ЗАПИСИ = " + rowID);

        } catch (Throwable t) {
            Log.d(TAG, "Ошибка при добалении адреса в базу данных: " + t.toString());
        }
        dbHelperSrc.close();
    }
}

