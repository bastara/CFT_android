package com.example.newsaggregator.ui;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.R;
import com.example.newsaggregator.db.DBHelper;
import com.example.newsaggregator.db.NewsContract;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    Handler handler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        String url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.webView);
//        поддержка JavaScript
//        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new MyWebViewClient());

        webView.loadUrl(url);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    private String srcRSS = "";
    private boolean pageIsLoaded = false;

    public void onClickBack(MenuItem item) {
        Intent intent = new Intent(WebActivity.this,
                MainActivity.class);
        startActivity(intent);
    }

    public void onClick(MenuItem item) {
        if (pageIsLoaded) {
            doWork(srcRSS);
            //TODO разобратья с утечками памяти
            handler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    Toast.makeText(getApplicationContext(), srcRSS, Toast.LENGTH_SHORT).show();
                    if (msg.what == 1) {
                        Toast.makeText(getApplicationContext(), "RSS файл добавлен в вашу библиотеку", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Это не RSS файл", Toast.LENGTH_SHORT).show();
                    }
                }

                ;
            };
        } else {
            Toast.makeText(getApplicationContext(), "Дождитесь полной загрузки страницы", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        // Для старых устройств
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Toast.makeText(getApplicationContext(), "Страница загружена!", Toast.LENGTH_SHORT).show();
            srcRSS = url;
            pageIsLoaded = true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Toast.makeText(getApplicationContext(), "Начата загрузка страницы", Toast.LENGTH_SHORT)
                    .show();
            pageIsLoaded = false;
        }
    }

    private void doWork(final String src) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AddPage.addPage(src, WebActivity.this);
//                ParseXML.parseXML(src, WebActivity.this);
//            }
//        });
//        thread.start();
//TODO можно подумать над возвращаемым значением и над добавлением через потоки-что б активити не терялось
        AddPage addPage = new AddPage(src, WebActivity.this);
//        AddPage.addPage(src, WebActivity.this);
        addPage.addPage();
        Toast.makeText(getApplicationContext(), "отработал AddPage", Toast.LENGTH_SHORT).show();
    }

    public class AddPage {
        Context context;
        String src;

        AddPage(String src, Context context) {
            this.context = context;
            this.src = src;
        }

        void addPage() {
            DBHelper dbHelper = new DBHelper(context);

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            final String TAG = "rssDB";
            ContentValues cv = new ContentValues();

            if (!src.startsWith("http://") && !src.startsWith("https://")) {
                src = "http://" + src;
            }

            Thread thread = new Thread(new CheckRSS(src));

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            try {
                Log.d(TAG, "Добавляю в источники RSS " + src);
                String tmpStr = src.substring(src.indexOf("//") + 2, src.indexOf(("/"), 8));
                cv.put(NewsContract.NewsEntry.COLUMN_URL, tmpStr);

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

        private void doWork(final String src, final Context context) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ParseXML parseXML = new ParseXML(src, context);
                    parseXML.parseXML();
//                    ParseXML.parseXML(src, context);
                }
            });
            thread.start();
        }
    }

    public class ParseXML {
        //TODO мб это соединить с предыдущим классом?
        Context context;
        String src;

        ParseXML(String src, Context context) {
            this.context = context;
            this.src = src;
        }

        void parseXML() {
            final String TAG = "ЛогКот";
            DBHelper dbHelper = new DBHelper(context);

            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();

            try {
                URL url = new URL(src);
                InputStream inputStream = url.openConnection().getInputStream();

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[224];
                int length;
                if ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                    if (result.toString("UTF-8").contains("rss")) {
                        Log.d(TAG, result.toString("UTF-8"));
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                        return;
                    }
                }

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
                        String tmpStr = src.substring(src.indexOf("//") + 2, src.indexOf(("/"), 8));
                        cv.put(NewsContract.NewsEntry.COLUMN_URL, tmpStr);
                        long rowID = database.insert(NewsContract.NewsEntry.TABLE_NEWS, null, cv);
                        Log.d(TAG, "НОМЕР ЗАПИСИ = " + rowID);
                        MainActivity.newCursor = true;
                        isItem = false;
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
                        cv.put(NewsContract.NewsEntry.COLUMN_LINK_NEWS, parser.getText());
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


    class CheckRSS implements Runnable {
        public String link;

        public CheckRSS(String link) {
            this.link = link;
        }

        @Override
        public void run() {
            System.out.println("Поток начал работу:::" + Thread.currentThread().getName());
            try {
                URL url = new URL(link);
                InputStream inputStream = url.openConnection().getInputStream();

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[224];
                int length;
                if ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                    if (result.toString("UTF-8").contains("rss")) {
                        Log.d(TAG, result.toString("UTF-8"));
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                        return;
                    }
                }
            } catch (IOException t) {
                Log.d(TAG, "Ошибка при праверке документа на RSS: " + t.toString());
            }
        }
    }
}
