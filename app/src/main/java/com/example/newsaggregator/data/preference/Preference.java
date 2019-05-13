package com.example.newsaggregator.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.newsaggregator.data.Contract;

import static android.content.Context.MODE_PRIVATE;

public class Preference {

    private SharedPreferences settings;

    public Preference(Context context) {
        settings = context.getSharedPreferences(Contract.Entry.PREFERENCES_FILE, MODE_PRIVATE);
    }

    public int getTimeRefresh() {
        return settings.getInt(Contract.Entry.PREFERENCES_TIME_REFRESH, 900000);
    }

    public void setTimeRefresh(int timeRefresh) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Contract.Entry.PREFERENCES_TIME_REFRESH, timeRefresh);
        editor.apply();
    }

    public boolean getNotification() {
        return settings.getBoolean(Contract.Entry.PREFERENCES_NOTIFICATION, true);
    }

    public void setNotification(boolean notification) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Contract.Entry.PREFERENCES_NOTIFICATION, notification);
        editor.apply();
    }
}

