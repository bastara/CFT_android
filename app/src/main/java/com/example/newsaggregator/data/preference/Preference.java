package com.example.newsaggregator.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Preference {

    private static final String APP_PREFERENCES = "mysettings";
    private static final String APP_PREFERENCES_TIME_REFRESH = "refresher";
    private SharedPreferences settings;

    public Preference(Context context) {
        settings = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
    }

    public String getTimeRefresh() {
        String timeRefresh;
        if (settings.contains(APP_PREFERENCES_TIME_REFRESH)) {
            timeRefresh = settings.getString(APP_PREFERENCES_TIME_REFRESH, String.valueOf(0));
        } else timeRefresh = "900000";
        return timeRefresh;
    }

    public void setTimeRefresh(String timeRefresh) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APP_PREFERENCES_TIME_REFRESH, timeRefresh);
        editor.apply();
    }
}

