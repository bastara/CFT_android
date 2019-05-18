package com.example.newsaggregator.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import com.example.newsaggregator.MyApplication;
import com.example.newsaggregator.data.Contract;

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

            boolean isItem = false;
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("item")) {
                    isItem = true;
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.END_TAG
                        && parser.getName()
                                 .equals("item")) {
                    Log.d(Contract.Entry.TAG, "Вношу данные БД ");
                    cv.put(Contract.Entry.COLUMN_URL, src);
                    if (cv.get(Contract.Entry.COLUMN_LINK_NEWS) != null) {
                        long rowID = database.insert(Contract.Entry.TABLE_NEWS, null, cv);
                        Log.d(Contract.Entry.TAG, "НОМЕР ЗАПИСИ = " + rowID);
                        isItem = false;
                    }
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("title")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(Contract.Entry.COLUMN_TITLE, parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("link")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    String tmpStr = parser.getText();


                    // Проверкак обновления новостей
//                    if (tmpStr.equals("http://www.garant.ru/news/1272827/")) {
//                        tmpStr = null;
//                        cv.put(Contract.Entry.COLUMN_LINK_NEWS, tmpStr);
//                        parser.next();
//                        continue;
//                    }

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
                                 .equals("description")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(Contract.Entry.COLUMN_DESCRIPTION, parser.getText()
                                                                    .replaceAll("\\<.*?\\>", "")
                                                                    .replaceAll("\n", " ")
                                                                    .replace("&quot;", "\""));
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("category")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(Contract.Entry.COLUMN_CATEGORY, parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("pubDate")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    cv.put(Contract.Entry.COLUMN_PUB_DATE, parser.getText());

//                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd - hh:mm:ss");
//                    String result =  sdf.format(this.getPubDate());
                }
                parser.next();
            }
        } catch (Throwable t) {
            Log.d(Contract.Entry.TAG, "Ошибка при загрузке XML-документа: " + t.toString());
        }
    }
}