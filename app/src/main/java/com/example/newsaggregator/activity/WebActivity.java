package com.example.newsaggregator.activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.data.db.DBAdapter;
import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.R;
import com.example.newsaggregator.data.db.ParseXML;
import com.example.newsaggregator.data.preference.Preference;
import com.example.newsaggregator.handler.MyHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    Handler handler;

    Preference preference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        preference = new Preference(WebActivity.this);
        preference.setLastScreen("WebActivity");

        final String url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.webView);
//        поддержка JavaScript
//        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);

        handler = new MyHandler(this);
    }

    public void handleMessage(android.os.Message msg) {
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT)
             .show();
        if (msg.what == Contract.Entry.SOURCE_ADDED) {
            Toast.makeText(getApplicationContext(), "Ресурс добавлен в вашу библиотеку", Toast.LENGTH_SHORT)
                 .show();
        } else if (msg.what == Contract.Entry.SOURCE_ALREADY_ADDED) {
            Toast.makeText(getApplicationContext(), "Данный ресурс уже добавлен!", Toast.LENGTH_SHORT)
                 .show();
        }
        if (msg.what == Contract.Entry.SOURCE_NOT_VALID) {
            Toast.makeText(getApplicationContext(), "Это не RSS-ATOM ресурс", Toast.LENGTH_SHORT)
                 .show();
        }
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

    public void onClickBack(MenuItem item) {
        Intent intent = new Intent(WebActivity.this,
                MainActivity.class);
        preference.setLastScreen("MainActivity");
        startActivity(intent);
    }

    private String url = "";
    private boolean pageIsLoaded = false;

    public void onClick(MenuItem item) {
        if (pageIsLoaded) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    CheckRSS checkRSS = new CheckRSS(url);
                    checkRSS.checkRSS();
                }
            });
            thread.start();
        } else {
            Toast.makeText(getApplicationContext(), "Дождитесь полной загрузки страницы!", Toast.LENGTH_SHORT)
                 .show();
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
            view.loadUrl(request.getUrl()
                                .toString());
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Toast.makeText(getApplicationContext(), "Страница загружена!", Toast.LENGTH_SHORT)
                 .show();
            WebActivity.this.url = url;
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

    public class AddPage {
        Context context;
        String src;

        AddPage(String src, Context context) {
            this.context = context;
            this.src = src;
        }

        String addPage() {
            DBAdapter DBAdapter = (DBAdapter) context.getApplicationContext();
            SQLiteDatabase dataBase = DBAdapter.getDatabase();

            final String TAG = "rssDB";
            ContentValues cv = new ContentValues();

            if (!src.startsWith("http://") && !src.startsWith("https://")) {
                src = "http://" + src;
            }

            try {
                Log.d(TAG, "Добавляю в источники RSS " + src);
                cv.put(Contract.Entry.COLUMN_URL, src);

                Log.d(TAG, "Вношу данные БД сайта");
                long rowID = dataBase.insert(Contract.Entry.TABLE_SITES, null, cv);
                if (rowID == -1) {
                    Log.d(TAG, "Данный ресурс уже добавлен ");
                    handler.sendEmptyMessage(Contract.Entry.SOURCE_ALREADY_ADDED);
                } else {
                    handler.sendEmptyMessage(Contract.Entry.SOURCE_ADDED);
                    Log.d(TAG, "НОМЕР ЗАПИСИ САЙТА = " + rowID);
                }
            } catch (SQLException t) {
                Log.d(TAG, "Ошибка при добалении адреса в базу данных: " + t.toString());
            }
//            doWork(src, context);
            return src;
        }

//        private void doWork(final String src, final Context context) {
//            ParseXML.parseXML(src, context);
//        }
    }

    class CheckRSS {
        public String link;

        CheckRSS(String link) {
            this.link = link;
        }

        void checkRSS() {
            System.out.println("Поток начал работу:::" + Thread.currentThread()
                                                               .getName());
            try {
                URL url = new URL(link);
                InputStream inputStream = url.openConnection()
                                             .getInputStream();

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[224];
                int length;
                Log.d(TAG, "Начало проверки RSS-ATOM: ");
                if ((length = inputStream.read(buffer)) != -1) {
                    Log.d(TAG, "итерация проверки : ");
                    result.write(buffer, 0, length);
                    if (result.toString("UTF-8")
                              .toLowerCase()
                              .contains("rss")) {
                        Log.d(TAG, result.toString("UTF-8"));
                        AddPage addPage = new AddPage(link, WebActivity.this);
                        ParseXML.parseXML(addPage.addPage(), getApplicationContext().getApplicationContext());
                    } else if (result.toString("UTF-8")
                                     .toLowerCase()
                                     .contains("atom")) {
                        Log.d(TAG, result.toString("UTF-8"));
                        AddPage addPage = new AddPage(link, WebActivity.this);
                        ParseAtom.parseAtom(addPage.addPage(), getApplicationContext().getApplicationContext());
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }
            } catch (IOException t) {
                Log.d(TAG, "Ошибка при проверке документа на RSS: " + t.toString());
            }
        }
    }
}
