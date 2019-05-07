package com.example.newsaggregator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.newsaggregator.R;

public class RefreshActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
    }

    public final static String REFRESH_TIME = "com.example.newsAggregator.REFRESH_TIME";

    public void onRadioClick(View v) {
        Intent answerIntent = new Intent();
        switch (v.getId()) {
            case R.id.radio15min:
                answerIntent.putExtra(REFRESH_TIME, "900000");
                break;
            case R.id.radio60min:
                answerIntent.putExtra(REFRESH_TIME, "3600000");
                break;
            case R.id.radio6hours:
                answerIntent.putExtra(REFRESH_TIME, "21600000");
                break;
            case R.id.radio24hours:
                answerIntent.putExtra(REFRESH_TIME, "86400000");
                break;
            default:
                break;
        }
        setResult(RESULT_OK, answerIntent);
        finish();
    }
}