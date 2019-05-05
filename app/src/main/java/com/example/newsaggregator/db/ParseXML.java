package com.example.newsaggregator.db;

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

//TODO данный класс не работает, ссылка в addPage
public class ParseXML {
    //TODO мб это соединить с предыдущим классом?
//    Context context;
//    String src;
//
//    public ParseXML(String src, Context context) {
//        this.context = context;
//        this.src = src;
//    }

    public static void parseXML(String src, Context context) {
        final String TAG = "ЛогКот";
        DBHelper dbHelper = new DBHelper(context);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        Cursor cursor;
        try {
            URL url = new URL(src);
            InputStream inputStream = url.openConnection().getInputStream();
//
//                ByteArrayOutputStream result = new ByteArrayOutputStream();
//                byte[] buffer = new byte[224];
//                int length;
//                if ((length = inputStream.read(buffer)) != -1) {
//                    result.write(buffer, 0, length);
//                    if (result.toString("UTF-8").contains("rss")) {
//                        Log.d(TAG, result.toString("UTF-8"));
//                        handler.sendEmptyMessage(1);
//                    } else {
//                        handler.sendEmptyMessage(0);
//                        return;
//                    }
//                }

            inputStream = url.openConnection().getInputStream();

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
//                    Log.d(TAG, "название = " + parser.getText());
                    cv.put(NewsContract.NewsEntry.COLUMN_TITLE, parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("link")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
//                    Log.d(TAG, "ссылка = " + parser.getText());
                    String tmpStr = parser.getText();
                    cursor = database.query(NewsContract.NewsEntry.TABLE_NEWS, null, NewsContract.NewsEntry.COLUMN_LINK_NEWS + "=?", new String[]{tmpStr}, null, null, null);
//                    String tmpLink = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_LINK_NEWS));
                    if (cursor.moveToFirst()) {
                        continue;
                    }
                    cv.put(NewsContract.NewsEntry.COLUMN_LINK_NEWS, tmpStr);
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("description")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
//                    Log.d(TAG, "описание = " + getDescription(parser.getText()));
//                    Log.d(TAG, "описание = " + parser.getText().replaceAll("\\<.*?\\>", "").replaceAll("\n", " "));
                    cv.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, parser.getText().replaceAll("\\<.*?\\>", "").replaceAll("\n", " "));
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
//                    Log.d(TAG, "категория = " + parser.getText());
                    cv.put(NewsContract.NewsEntry.COLUMN_CATEGORY, parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("pubDate")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
//                    Log.d(TAG, "дата = " + parser.getText());
                    cv.put(NewsContract.NewsEntry.COLUMN_PUBDATE, parser.getText());
                }
                parser.next();
            }

        } catch (Throwable t) {
            Log.d(TAG, "Ошибка при загрузке XML-документа: " + t.toString());
        }
        dbHelper.close();
    }
}