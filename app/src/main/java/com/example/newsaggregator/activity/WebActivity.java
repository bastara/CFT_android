package com.example.newsaggregator.activity;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.R;
import com.example.newsaggregator.db.AddPage;

public class WebActivity extends AppCompatActivity {
    private WebView webView;

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
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    private String srcRSS = "";
    private boolean pageIsLoaded = false;

    public void onClick(MenuItem item) {
        if (pageIsLoaded) {
            Toast.makeText(getApplicationContext(), srcRSS, Toast.LENGTH_SHORT).show();
            if (srcRSS.substring(srcRSS.length() - 4).equals(".xml") || srcRSS.contains("rss") || srcRSS.contains("feed")) {
                doWork(srcRSS);

                Toast.makeText(getApplicationContext(), "RSS файл добавлен в вашу библиотеку", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Это не RSS файл", Toast.LENGTH_SHORT).show();
            }
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
        AddPage.addPage(src, WebActivity.this);
        Toast.makeText(getApplicationContext(), "отработал AddPage", Toast.LENGTH_SHORT).show();
    }
}