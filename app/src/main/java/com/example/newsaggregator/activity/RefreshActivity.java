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

    public final static String THIEF = "com.example.newsAggregator.THIEF";

    public void onRadioClick(View v) {
        Intent answerIntent = new Intent();
        //время оставил в таком формате по причине вывода этих данных в меню
        //иначе там бы пришлось из милисекунд переводить в наглядный вид
        switch (v.getId()) {
            case R.id.radio15min:
                answerIntent.putExtra(THIEF, "15min");
                break;
            case R.id.radio60min:
                answerIntent.putExtra(THIEF, "60min");
                break;
            case R.id.radio6hours:
                answerIntent.putExtra(THIEF, "6h");
                break;
            case R.id.radio24hours:
                answerIntent.putExtra(THIEF, "24h");
                break;

            default:
                break;
        }
        setResult(RESULT_OK, answerIntent);
        finish();
    }
}