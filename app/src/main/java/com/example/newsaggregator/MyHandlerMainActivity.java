package com.example.newsaggregator;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

class MyHandlerMainActivity extends Handler {

    private WeakReference wrActivity;

    MyHandlerMainActivity(MainActivity activity) {
        wrActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        MainActivity activity = (MainActivity) wrActivity.get();
        if (activity != null)
            activity.handleMessage(msg);
    }
}
