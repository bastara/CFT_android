package com.example.newsaggregator.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.newsaggregator.data.db.DBAdapter;
import com.example.newsaggregator.R;
import com.example.newsaggregator.data.db.NewsContract;

public class NewsActivity extends AppCompatActivity {

    TextView nameText;
    TextView dateText;
    TextView linkText;
    TextView textText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        nameText = findViewById(R.id.textName);
        dateText = findViewById(R.id.textDate);
        linkText = findViewById(R.id.textLink);
        textText = findViewById(R.id.textText);
        textText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String position = intent.getStringExtra("id");

        DBAdapter dbAdapter = (DBAdapter) this.getApplication();

        Cursor cursor = dbAdapter.getCursorNews();

        cursor.moveToPosition(Integer.parseInt(position));

        String name = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
        String date = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBDATE)).substring(0, 16);
        String link = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_LINK_NEWS));
        link = link.substring(link.indexOf("//") + 2, link.indexOf(("/"), 8));
        String text = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION));

        nameText.setText(name);
        dateText.setText(date);
        linkText.setText(link);
        textText.setText(text);
    }
}
