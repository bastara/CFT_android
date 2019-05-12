package com.example.newsaggregator.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.URL;

public class ParseXML {
    public static void parseXML(final String src, final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork(src, context);
            }
        });
        thread.start();
    }

    private static void doWork(String src, Context context) {
        MySingleton mySingleton = (MySingleton) context.getApplicationContext();

        SQLiteDatabase database = mySingleton.getDatabase();

        ContentValues cv = new ContentValues();
        Cursor cursor;
        try {
            URL url = new URL(src);
            InputStream inputStream = url.openConnection().getInputStream();

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            boolean isItem = false;
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("item")) {
                    isItem = true;
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.END_TAG
                        && parser.getName().equals("item")) {
                    Log.d(Contract.Entry.TAG, "Вношу данные БД ");
//                        String tmpStr = src.substring(src.indexOf("//") + 2, src.indexOf(("/"), 8));
//                        cv.put(Contract.Entry.COLUMN_URL, tmpStr);
                    cv.put(Contract.Entry.COLUMN_URL, src);
                    if (cv.get(Contract.Entry.COLUMN_LINK_NEWS) != null) {
                        long rowID = database.insert(Contract.Entry.TABLE_NEWS, null, cv);
                        Log.d(Contract.Entry.TAG, "НОМЕР ЗАПИСИ = " + rowID);
//                        MainActivity.newCursor = true;
                        isItem = false;
                    }
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("title")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(Contract.Entry.COLUMN_TITLE, parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("link")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    String tmpStr = parser.getText();


                    // Проверкак обновления новостей
                    if (tmpStr.equals("http://2tura.ru/2лл019/05/10/%d0%b2%d0%b8%d0%b4-%d0%bd%d0%b0-%d0%b4%d0%be%d0%bb%d0%b8%d0%bd%d1%83-%d0%bd%d0%be%d1%80%d0%b2%d0%b5%d0%b3%d0%b8%d1%8f/")) {
                        tmpStr = null;
                        cv.put(Contract.Entry.COLUMN_LINK_NEWS, tmpStr);
                        continue;
                    }
//                    cursor = database.query(Contract.Entry.TABLE_NEWS, null, Contract.Entry.COLUMN_LINK_NEWS + "=?", new String[]{tmpStr}, null, null, null);
                    cursor = mySingleton.getCursorCheckSite(tmpStr);
                    if (cursor.moveToFirst()) {
                        Log.d(Contract.Entry.TAG, "Данная новость уже добавлена " + tmpStr);
                        continue;
                    }
                    cv.put(Contract.Entry.COLUMN_LINK_NEWS, tmpStr);
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("description")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(Contract.Entry.COLUMN_DESCRIPTION, parser.getText().replaceAll("\\<.*?\\>", "").replaceAll("\n", " "));
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(Contract.Entry.COLUMN_CATEGORY, parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("pubDate")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(Contract.Entry.COLUMN_PUBDATE, parser.getText());
                }
                parser.next();
            }
        } catch (Throwable t) {
            Log.d(Contract.Entry.TAG, "Ошибка при загрузке XML-документа: " + t.toString());
        }
    }
}