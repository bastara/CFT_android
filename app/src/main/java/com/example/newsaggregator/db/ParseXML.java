package com.example.newsaggregator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.example.newsaggregator.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;

public class ParseXML {
    private final String TAG = "ЛогКот";
    DBHelper dbHelper;

    public static void parseXML(String src, Context context) {
        final String TAG = "rssDB";
        DBHelper dbHelper = new DBHelper(context);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(src));

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
                    long rowID = database.insert("newsss", null, cv);
                    Log.d(TAG, "НОМЕР ЗАПИСИ = " + rowID);
                    isItem = false;
                    parser.next();
                    continue;
                }

                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("title")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    Log.d(TAG, "название = " + parser.getText());
                    cv.put("title", parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("link")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    Log.d(TAG, "ссылка = " + parser.getText());
                    cv.put("link", parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("description")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
//                    Log.d(TAG, "описание = " + getDescription(parser.getText()));
                    Log.d(TAG, "описание = " + parser.getText().replaceAll("\\<.*?\\>", "").replaceAll("\n", " "));
                    cv.put("description", parser.getText().replaceAll("\\<.*?\\>", "").replaceAll("\n", " "));
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    Log.d(TAG, "категория = " + parser.getText());
                    cv.put("category", parser.getText());
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("pubDate")
                        && parser.next() == XmlPullParser.TEXT
                        && isItem) {
                    Log.d(TAG, "дата = " + parser.getText());
                    cv.put("pubDate", parser.getText());
                }
                parser.next();
            }

        } catch (Throwable t) {
            Toast.makeText(this,
                    "Ошибка при загрузке XML-документа: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
        dbHelper.close();
    }
}
