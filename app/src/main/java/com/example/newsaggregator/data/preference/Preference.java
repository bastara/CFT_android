package com.example.newsaggregator.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.newsaggregator.data.Contract;

import static android.content.Context.MODE_PRIVATE;

public class Preference {

    private final SharedPreferences settings;

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

    public String getLastScreen() {
        return settings.getString(Contract.Entry.PREFERENCES_LAST_SCREEN, "MainActivity");
    }

    public void setLastScreen(String lastScreen) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Contract.Entry.PREFERENCES_LAST_SCREEN, lastScreen);
        editor.apply();
    }

    public String getLastSite() {
        return settings.getString(Contract.Entry.PREFERENCES_LAST_SITE, "www.yandex.ru");
    }

    public void setLastSite(String lastSite) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Contract.Entry.PREFERENCES_LAST_SITE, lastSite);
        editor.apply();
    }

    public String getUserURL() {
        return settings.getString(Contract.Entry.PREFERENCES_URL, "http://rss.garant.ru/article/");
    }

    public void setUserURL(String url) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Contract.Entry.PREFERENCES_URL, url);
        editor.apply();
    }

    public boolean getTheme() {
        return settings.getBoolean(Contract.Entry.PREFERENCES_THEME, true);
    }

    public void setTheme(boolean theme) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Contract.Entry.PREFERENCES_THEME, theme);
        editor.apply();
    }

    public String getLocale() {
        return settings.getString(Contract.Entry.PREFERENCES_LOCALE, "ru");
    }

    public void setLocale(String locale) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Contract.Entry.PREFERENCES_LOCALE, locale);
        editor.apply();
    }
}

