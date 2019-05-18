package com.example.newsaggregator.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.newsaggregator.data.db.DBRequest;
import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.R;
import com.example.newsaggregator.data.preference.Preference;

public class NewsActivity extends AppCompatActivity {

    TextView nameText;
    TextView dateText;
    TextView linkText;
    TextView textText;

    Preference preference;

    public void onCreate(Bundle savedInstanceState) {

        preference = new Preference(NewsActivity.this);

        if (preference.getTheme()) {
            setTheme(R.style.DT);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        nameText = findViewById(R.id.textName);
        dateText = findViewById(R.id.textDate);
        linkText = findViewById(R.id.textLink);
        textText = findViewById(R.id.textText);
        textText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String position = intent.getStringExtra("id");

        DBRequest dbRequest = new DBRequest(NewsActivity.this);
        Cursor cursor = dbRequest.getCursorNews();

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
