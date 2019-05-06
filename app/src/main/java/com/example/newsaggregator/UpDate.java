package com.example.newsaggregator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsaggregator.db.DBHelper;
import com.example.newsaggregator.db.NewsContract;
import com.example.newsaggregator.db.ParseXML;

class UpDate {

    public static void upDate(final Context context) {
        Cursor cursor;
        DBHelper dbHelper = new DBHelper(context);

        SQLiteDatabase dataBase = dbHelper.getReadableDatabase();

        cursor = dataBase.rawQuery("select * from " + NewsContract.NewsEntry.TABLE_SITES, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToFirst();
            final String url = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL));
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ParseXML.parseXML(url, context);
                }
            });
            thread.start();

        }
        MainActivity.newCursor = true;
        cursor.close();
        dataBase.close();
        dbHelper.close();
    }
}
