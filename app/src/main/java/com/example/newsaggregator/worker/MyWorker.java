package com.example.newsaggregator.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.newsaggregator.network.Update;

import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {
    private static final String TAG = "rss1LogsUniq4";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: start " + Thread.currentThread().getName());

        try {
            Update update = new Update();
            update.upDate(getApplicationContext(), false);
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "doWork: end " + Thread.currentThread().getName());
        return Result.success();
    }
}
