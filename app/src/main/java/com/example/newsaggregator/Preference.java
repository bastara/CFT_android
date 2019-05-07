package com.example.newsaggregator;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

class Preference {

    private static final String APP_PREFERENCES = "mysettings";
    private static final String APP_PREFERENCES_TIME_REFRESH = "refresher";
    private SharedPreferences settings;

    Preference(Context context) {
        settings = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
    }

    public String getTimeRefresh() {
        String timeRefresh;
        if (settings.contains(APP_PREFERENCES_TIME_REFRESH)) {
            timeRefresh = settings.getString(APP_PREFERENCES_TIME_REFRESH, String.valueOf(0));
        } else timeRefresh = "15min";
        return timeRefresh;
    }

    public void setTimeRefresh(String timeRefresh) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APP_PREFERENCES_TIME_REFRESH, timeRefresh);
        editor.apply();
    }
}

