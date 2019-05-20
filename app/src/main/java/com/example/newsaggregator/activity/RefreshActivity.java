package com.example.newsaggregator.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.newsaggregator.R;
import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.data.preference.Preference;

public class RefreshActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Preference preference = new Preference(RefreshActivity.this);

        if (preference.getTheme()) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
    }

    public void onRadioClick(View v) {
        Intent answerIntent = new Intent();
        switch (v.getId()) {
            case R.id.radio15min:
                answerIntent.putExtra(Contract.Entry.REFRESH_TIME, 900000);
                break;
            case R.id.radio60min:
                answerIntent.putExtra(Contract.Entry.REFRESH_TIME, 3600000);
                break;
            case R.id.radio6hours:
                answerIntent.putExtra(Contract.Entry.REFRESH_TIME, 21600000);
                break;
            case R.id.radio24hours:
                answerIntent.putExtra(Contract.Entry.REFRESH_TIME, 86400000);
                break;
            default:
                break;
        }
        setResult(RESULT_OK, answerIntent);
        finish();
    }
}