package com.example.newsaggregator.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Preference {

    private static final String APP_PREFERENCES = "mysettings";
    private static final String APP_PREFERENCES_TIME_REFRESH = "refresher";
    private static final String APP_PREFERENCES_NOTIFICATION = "notification";
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

    //TODO возможно лучше на boolean переделать
    public String getNotification() {
        String notification;
        if (settings.contains(APP_PREFERENCES_NOTIFICATION)) {
            notification = settings.getString(APP_PREFERENCES_NOTIFICATION, String.valueOf(1));
        } else notification = "вкл";
        return notification;
    }

    public void setNotification(String notification) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APP_PREFERENCES_NOTIFICATION, notification);
        editor.apply();
    }
}

