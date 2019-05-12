package com.example.newsaggregator.activity;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

class MyHandler extends Handler {

    WeakReference wrActivity;

    MyHandler(WebActivity activity) {
        wrActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        WebActivity activity = (WebActivity) wrActivity.get();
        if (activity != null)
            activity.handleMessage(msg);
    }
}
