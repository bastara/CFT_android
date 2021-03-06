package com.example.newsaggregator.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.network.Update;

import java.net.ConnectException;

public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(Contract.Entry.TAG, "worker: start " + Thread.currentThread()
                                                           .getName());
        Update update = new Update();
        try {
            update.upDate(getApplicationContext(), false);
            Log.d(Contract.Entry.TAG, "worker: end " + Thread.currentThread()
                                                             .getName());
            return Result.success();
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}
