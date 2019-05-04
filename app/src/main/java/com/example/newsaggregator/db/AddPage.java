package com.example.newsaggregator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


//TODO Отключил
public class AddPage {
    public static void addPage(String src, Context context) {
        final String TAG = "rssDB";
        DBHelper dbHelper = new DBHelper(context);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        try {
            Log.d(TAG, "Добавляю в источники RSS " + src);
            cv.put(NewsContract.NewsEntry.COLUMN_URL, src);

            Log.d(TAG, "Вношу данные БД ");
            long rowID = database.insert(NewsContract.NewsEntry.TABLE_SITES, null, cv);
            if (rowID == -1) {
                Log.d(TAG, "Данный ресурс уже добавлен ");
            } else {
                Log.d(TAG, "НОМЕР ЗАПИСИ = " + rowID);
            }
        } catch (Throwable t) {
            Log.d(TAG, "Ошибка при добалении адреса в базу данных: " + t.toString());
        }
        dbHelper.close();

        doWork(src, context);
    }

    private static void doWork(final String src, final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ParseXML.parseXML(src, context);
            }
        });
        thread.start();
    }
}

