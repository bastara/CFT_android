package com.example.newsaggregator.handler;

import android.os.Handler;
import android.os.Message;

import com.example.newsaggregator.activity.WebActivity;

import java.lang.ref.WeakReference;

public class MyHandler extends Handler {

    WeakReference wrActivity;

    public MyHandler(WebActivity activity) {
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