package com.example.newsaggregator.activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.MyApplication;
import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.R;
import com.example.newsaggregator.data.db.ParseAtom;
import com.example.newsaggregator.data.db.ParseXML;
import com.example.newsaggregator.data.preference.Preference;
import com.example.newsaggregator.handler.HandlerInterface;
import com.example.newsaggregator.handler.MyHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class WebActivity extends AppCompatActivity implements HandlerInterface {
    private WebView webView;
    private Handler handler;

    private Preference preference;

    public void onCreate(Bundle savedInstanceState) {
        preference = new Preference(WebActivity.this);

        if (preference.getTheme()) {
            setTheme(R.style.DarkThemeToolbar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        if (!preference.getTheme()) {
            Objects.requireNonNull(getSupportActionBar())
                   .setBackgroundDrawable(new ColorDrawable(0xFF757575));
        }

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

    class AddPage {
        final Context context;
        String src;
        final String type;

        AddPage(String src, String type, Context context) {
            this.context = context;
            this.src = src;
            this.type = type;
        }

        String addPage() {
            MyApplication myApplication = (MyApplication) getApplication();

            ContentValues cv = new ContentValues();

            if (!src.startsWith("http://") && !src.startsWith("https://")) {
                src = "http://" + src;
            }

            try {
                Log.d(Contract.Entry.TAG, "Добавляю ресурсы  " + src);
                cv.put(Contract.Entry.COLUMN_URL, src);
                cv.put(Contract.Entry.COLUMN_TYPE_RESOURCE, type);

                Log.d(Contract.Entry.TAG, "Вношу данные БД сайта");
                long rowID = myApplication.getDatabase()
                                          .insert(Contract.Entry.TABLE_SITES, null, cv);
                if (rowID == -1) {
                    handler.sendEmptyMessage(Contract.Entry.SOURCE_ALREADY_ADDED);
                    Log.d(Contract.Entry.TAG, "Данный ресурс уже добавлен ");
                } else {
                    handler.sendEmptyMessage(Contract.Entry.SOURCE_ADDED);
                    Log.d(Contract.Entry.TAG, "НОМЕР ЗАПИСИ САЙТА = " + rowID);
                }
            } catch (SQLException t) {
                Log.d(Contract.Entry.TAG, "Ошибка при добалении адреса в базу данных: " + t.toString());
            }
            return src;
        }
    }

    class CheckRSS {
        final String src;

        CheckRSS(String src) {
            this.src = src;
        }

        void checkRSS() {
            try {
                URL url = new URL(src);
                InputStream inputStream = url.openConnection()
                                             .getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);
                parser.next();

                if (parser.getName()
                          .equalsIgnoreCase("rss")) {
                    AddPage addPage = new AddPage(src, "RSS", WebActivity.this);
                    ParseXML.parseXML(addPage.addPage(), getApplicationContext().getApplicationContext());
                } else if (parser.getName()
                                 .equalsIgnoreCase("feed")) {
                    AddPage addPage = new AddPage(src, "ATOM", WebActivity.this);
                    ParseAtom.parseAtom(addPage.addPage(), getApplicationContext().getApplicationContext());
                } else {
                    handler.sendEmptyMessage(0);
                }

            } catch (IOException t) {
                Log.d(TAG, "Ошибка при проверке документа на RSS: " + t.toString());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }
}
