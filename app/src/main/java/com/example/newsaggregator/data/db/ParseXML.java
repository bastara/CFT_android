package com.example.newsaggregator.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import com.example.newsaggregator.MainActivity;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.URL;

public class ParseXML {
    public static void parseXML(String src, Context context) {
        final String TAG = "rssDB";

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
                    Log.d(TAG, "Вношу данные БД ");
//                        String tmpStr = src.substring(src.indexOf("//") + 2, src.indexOf(("/"), 8));
//                        cv.put(NewsContract.NewsEntry.COLUMN_URL, tmpStr);
                    cv.put(NewsContract.NewsEntry.COLUMN_URL, src);
                    if (cv.get(NewsContract.NewsEntry.COLUMN_LINK_NEWS) != null) {
                        long rowID = database.insert(NewsContract.NewsEntry.TABLE_NEWS, null, cv);
                        Log.d(TAG, "НОМЕР ЗАПИСИ = " + rowID);
                        MainActivity.newCursor = true;
                        isItem = false;
                    }
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("title")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(NewsContract.NewsEntry.COLUMN_TITLE, parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("link")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    String tmpStr = parser.getText();


                    // Проверкак обновления новостей
//                    if (tmpStr.equals("http://www.garant.ru/article/1265576/")) {
//                        tmpStr = null;
//                        cv.put(NewsContract.NewsEntry.COLUMN_LINK_NEWS, tmpStr);
//                        continue;
////                    }
//                    cursor = database.query(NewsContract.NewsEntry.TABLE_NEWS, null, NewsContract.NewsEntry.COLUMN_LINK_NEWS + "=?", new String[]{tmpStr}, null, null, null);
                    cursor = mySingleton.getCursorCheckSite(tmpStr);
                    if (cursor.moveToFirst()) {
                        continue;
                    }
                    cv.put(NewsContract.NewsEntry.COLUMN_LINK_NEWS, tmpStr);
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("description")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, parser.getText().replaceAll("\\<.*?\\>", "").replaceAll("\n", " "));
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(NewsContract.NewsEntry.COLUMN_CATEGORY, parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("pubDate")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(NewsContract.NewsEntry.COLUMN_PUBDATE, parser.getText());
                }
                parser.next();
            }
        } catch (Throwable t) {
            Log.d(TAG, "Ошибка при загрузке XML-документа: " + t.toString());
        }
    }
}