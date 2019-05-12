package com.example.newsaggregator.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.newsaggregator.data.db.Contract;
import com.example.newsaggregator.data.db.MySingleton;
import com.example.newsaggregator.R;

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

        MySingleton mySingleton = (MySingleton) this.getApplication();

        Cursor cursor = mySingleton.getCursorNews();

        cursor.moveToPosition(Integer.parseInt(position));

        String name = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_TITLE));
        String date = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_PUBDATE)).substring(0, 16);
        String link = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_LINK_NEWS));
        link = link.substring(link.indexOf("//") + 2, link.indexOf(("/"), 8));
        String text = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_DESCRIPTION));

        nameText.setText(name);
        dateText.setText(date);
        linkText.setText(link);
        textText.setText(text);
    }
}
