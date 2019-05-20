package com.example.newsaggregator.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newsaggregator.R;
import com.example.newsaggregator.data.preference.Preference;

public class AddSiteActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView7;
    private TextView textView8;

    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preference = new Preference(AddSiteActivity.this);

        if (preference.getTheme()) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);

        preference = new Preference(AddSiteActivity.this);
        preference.setLastScreen("AddSiteActivity");

        editText = findViewById(R.id.editSiteAddress);
        editText.setText(preference.getUserURL());
        editText.addTextChangedListener(urlWatcher);

        textView1 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);
        textView3 = findViewById(R.id.textView4);
        textView4 = findViewById(R.id.textView5);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", editText.getText()
                                       .toString());
        preference.setLastSite(editText.getText()
                                       .toString());
        startActivity(intent);
    }

    private final TextWatcher urlWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() != 0) {
                preference.setUserURL(editText.getText()
                                              .toString());
            }
        }
    };


    public void onClick2(View view) {
        editText.setText(textView1.getText()
                                  .toString());
    }

    public void onClick3(View view) {
        editText.setText(textView2.getText()
                                  .toString());
    }

    public void onClick4(View view) {
        editText.setText(textView3.getText()
                                  .toString());
    }

    public void onClick5(View view) {
        editText.setText(textView4.getText()
                                  .toString());
    }

    public void onClick7(View view) {
        editText.setText(textView7.getText()
                                  .toString());
    }

    public void onClick8(View view) {
        editText.setText(textView8.getText()
                                  .toString());
    }
}
