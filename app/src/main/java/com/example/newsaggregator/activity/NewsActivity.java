package com.example.newsaggregator.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.newsaggregator.MyApplication;
import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.R;
import com.example.newsaggregator.data.preference.Preference;

public class NewsActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {

        Preference preference = new Preference(NewsActivity.this);

        if (preference.getTheme()) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        TextView nameText = findViewById(R.id.textName);
        TextView dateText = findViewById(R.id.textDate);
        TextView linkText = findViewById(R.id.textLink);
        TextView textText = findViewById(R.id.textText);
        textText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String position = intent.getStringExtra("id");

        MyApplication myApplication = (MyApplication) getApplication();
        Cursor cursor = myApplication.dbRequest.getCursorNews();

        cursor.moveToPosition(Integer.parseInt(position));

        String name = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_TITLE));
        String date = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_PUB_DATE)).substring(0, 16);
        String link = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_LINK_NEWS));
        String text = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_DESCRIPTION));

        nameText.setText(name);
        dateText.setText(date);
        linkText.setText(link);
        textText.setText(text);
    }
}
