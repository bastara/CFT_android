package com.example.newsaggregator.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.data.db.DBAdapter;
import com.example.newsaggregator.data.db.DBRequest;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.URL;

class ParseAtom {

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
        DBAdapter DBAdapter = (DBAdapter) context.getApplicationContext();
        SQLiteDatabase database = DBAdapter.getDatabase();
        DBRequest dbRequest = new DBRequest(context);
        Cursor cursor;

        ContentValues cv = new ContentValues();

        String TAG = "ATOM-----";

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
                    Log.d(TAG, "________________________________");
                    cv.put(Contract.Entry.COLUMN_URL, src);
                    if (cv.get(Contract.Entry.COLUMN_LINK_NEWS) != null) {
//                        if (cv.get(Contract.Entry.COLUMN_CATEGORY) != null) {
//
//                        }
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
                    Log.d(TAG, "title      " + parser.getText());
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .contains("link")
                        && parser.getAttributeValue(1)
                                 .equals("alternate")
                        && isEntry) {
                    String tmpStr = parser.getAttributeValue(0);


                    // Проверкак обновления новостей
//                    if (tmpStr.equals("http://2tura.ru/2лл019/05/10/%d0%b2%d0%b8%d0%b4-%d0%bd%d0%b0-%d0%b4%d0%be%d0%bb%d0%b8%d0%bd%d1%83-%d0%bd%d0%be%d1%80%d0%b2%d0%b5%d0%b3%d0%b8%d1%8f/")) {
//                        tmpStr = null;
//                        cv.put(Contract.Entry.COLUMN_LINK_NEWS, tmpStr);
//                        continue;
//                    }
//                    cursor = database.query(Contract.Entry.TABLE_NEWS, null, Contract.Entry.COLUMN_LINK_NEWS + "=?", new String[]{tmpStr}, null, null, null);
                    cursor = dbRequest.getCursorCheckSite(tmpStr);
                    if (cursor.moveToFirst()) {
                        Log.d(Contract.Entry.TAG, "Данная новость уже добавлена " + tmpStr);
                        continue;
                    }
                    cv.put(Contract.Entry.COLUMN_LINK_NEWS, tmpStr);
                    Log.d(TAG, "link      " + parser.getAttributeValue(0));
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("content")
                        && parser.next() == XmlPullParser.TEXT
                        && isEntry) {
                    cv.put(Contract.Entry.COLUMN_DESCRIPTION, parser.getText()
                                                                    .replaceAll("\\<.*?\\>", "")
                                                                    .replaceAll("\n", " "));
                    Log.d(TAG, "content   " + parser.getText()
                                                    .replaceAll("\\<.*?\\>", "")
                                                    .replaceAll("\n", " "));
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("category")
                        && isEntry) {
                    cv.put(Contract.Entry.COLUMN_CATEGORY, parser.getAttributeValue(0));
                    Log.d(TAG, "category  " + parser.getAttributeValue(0));
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName()
                                 .equals("published")
                        && parser.next() == XmlPullParser.TEXT
                        && isEntry) {
                    cv.put(Contract.Entry.COLUMN_PUB_DATE, parser.getText());
                    Log.d(TAG, "published " + parser.getText());

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
