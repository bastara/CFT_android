package com.example.newsaggregator.data;

public class Contract {

    private Contract() {
    }

    public static final class Entry {
        //TAG
        public static final String TAG = "rss";

        //Bundle
        public static final String KEY_RECYCLER_STATE = "recycler_state";

        //DB
        public static final String DATABASE_NAME = "RSSDB24";
        public static final int DATABASE_VERSION = 1;

        public static final String TABLE_NEWS = "news";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK_NEWS = "link";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_IS_SHOW = "isShow";
        public static final String COLUMN_PUB_DATE = "pubDate";
        public static final String COLUMN_URL = "url";

        public static final String TABLE_SITES = "tableOfSite";

        public static final String COLUMN_TYPE_RESOURCE = "type";

        //Preference
        public static final String PREFERENCES_FILE = "my_settings_3";
        public static final String PREFERENCES_TIME_REFRESH = "aaaaaarefresher";
        public static final String PREFERENCES_NOTIFICATION = "notification";
        public static final String PREFERENCES_LAST_SCREEN = "lastScreen";
        public static final String PREFERENCES_LAST_SITE = "lastSite";
        public static final String PREFERENCES_URL = "userUrl";
        public static final String PREFERENCES_THEME = "theme";
        public static final String PREFERENCES_LOCALE = "locale";

        //Refresh time
        public final static String REFRESH_TIME = "com.example.newsAggregator.REFRESH_TIME";

        //Check resource
        public static final int SOURCE_ADDED = 2;
        public static final int SOURCE_ALREADY_ADDED = 1;
        public static final int SOURCE_NOT_VALID = 0;
    }
}
