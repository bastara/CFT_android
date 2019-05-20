package com.example.newsaggregator.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class MyHandler extends Handler {

    private final WeakReference<HandlerInterface> wrActivity;

    public MyHandler(HandlerInterface activity) {
        wrActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        HandlerInterface activity = wrActivity.get();
        if (activity != null)
            activity.handleMessage(msg);
    }
}