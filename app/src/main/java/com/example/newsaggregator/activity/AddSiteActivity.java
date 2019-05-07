package com.example.newsaggregator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newsaggregator.R;

public class AddSiteActivity extends AppCompatActivity {

    EditText editText;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);

        editText = findViewById(R.id.editSiteAddress);
        textView1 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);
        textView3 = findViewById(R.id.textView4);
        textView4 = findViewById(R.id.textView5);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", editText.getText().toString());
        startActivity(intent);
    }

    public void onClick2(View view) {
        editText.setText(textView1.getText().toString());
    }

    public void onClick3(View view) {
        editText.setText(textView2.getText().toString());
    }

    public void onClick4(View view) {
        editText.setText(textView3.getText().toString());
    }

    public void onClick5(View view) {
        editText.setText(textView4.getText().toString());
    }
}
