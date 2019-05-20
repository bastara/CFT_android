package com.example.newsaggregator.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.MyApplication;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.URL;

public class ParseAtom {

    public static void parseAtom(final String src, final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork(src, context);
            }
        });
        thread.start();
    }

    private static void doWork(String src, Context context) {
        MyApplication MyApplication = (MyApplication) context.getApplicationContext();
        SQLiteDatabase database = MyApplication.getDatabase();
        DBRequest dbRequest = new DBRequest(context);
        Cursor cursor;

        ContentValues cv = new ContentValues();

        try {
            URL url = new URL(src);
            InputStream inputStream = url.openConnection()
                                         .getInputStream();

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            boolean isEntry = false;
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("entry")) {
                    isEntry = true;
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.END_TAG
                        && parser.getName()
                                 .equals("entry")) {
                    Log.d(Contract.Entry.TAG, "Вношу данные БД ");
                    Log.d(Contract.Entry.TAG, "________________________________");
                    cv.put(Contract.Entry.COLUMN_URL, src);
                    if (cv.get(Contract.Entry.COLUMN_LINK_NEWS) != null) {
                        long rowID = database.insert(Contract.Entry.TABLE_NEWS, null, cv);
                        Log.d(Contract.Entry.TAG, "НОМЕР ЗАПИСИ = " + rowID);
                        isEntry = false;
                    }
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("title")
                        && parser.next() == XmlPullParser.TEXT
                        && isEntry) {
                    cv.put(Contract.Entry.COLUMN_TITLE, parser.getText());
                    Log.d(Contract.Entry.TAG, "title      " + parser.getText());
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .contains("link")
                        && isEntry) {
                    String tmpStr = null;
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        Log.d(Contract.Entry.TAG, "link      " + parser.getAttributeValue(i));
                        if (parser.getAttributeName(i)
                                  .equals("href")) {
                            tmpStr = parser.getAttributeValue(i);
                        }
                    }

                    cursor = dbRequest.getCursorCheckSite(tmpStr);
                    if (cursor.moveToFirst()) {
                        Log.d(Contract.Entry.TAG, "Данная новость уже добавлена " + tmpStr);
                        parser.next();
                        continue;
                    }
                    cv.put(Contract.Entry.COLUMN_LINK_NEWS, tmpStr);
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("summary")
                        && parser.next() == XmlPullParser.TEXT
                        && isEntry) {
                    cv.put(Contract.Entry.COLUMN_DESCRIPTION, parser.getText()
                                                                    .replaceAll("<.*?>", "")
                                                                    .replaceAll("\n", " ")
                                                                    .replace("&quot;", "\""));
                    Log.d(Contract.Entry.TAG, "summary   " + parser.getText()
                                                                   .replaceAll("<.*?>", "")
                                                                   .replaceAll("\n", " ")
                                                                   .replace("&quot;", "\""));
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("content")
                        && parser.next() == XmlPullParser.TEXT
                        && isEntry) {
                    cv.put(Contract.Entry.COLUMN_DESCRIPTION, parser.getText()
                                                                    .replaceAll("<.*?>", "")
                                                                    .replaceAll("\n", " ")
                                                                    .replace("&quot;", "\""));
                    Log.d(Contract.Entry.TAG, "content   " + parser.getText()
                                                                   .replaceAll("<.*?>", "")
                                                                   .replaceAll("\n", " ")
                                                                   .replace("&quot;", "\""));
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("category")
                        && isEntry) {
                    cv.put(Contract.Entry.COLUMN_CATEGORY, parser.getAttributeValue(0));
                    Log.d(Contract.Entry.TAG, "category  " + parser.getAttributeValue(0));
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("updated")
                        && parser.next() == XmlPullParser.TEXT
                        && isEntry) {
                    cv.put(Contract.Entry.COLUMN_PUB_DATE, parser.getText());
                    Log.d(Contract.Entry.TAG, "updated    " + parser.getText());
                }
                parser.next();
            }
        } catch (Throwable t) {
            Log.d(Contract.Entry.TAG, "Ошибка при загрузке XML-документа: " + t.toString());
        }
    }
}
