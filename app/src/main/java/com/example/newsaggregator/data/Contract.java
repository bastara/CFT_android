package com.example.newsaggregator.data;

public class Contract {

    private Contract() {
    }

    public static final class Entry {
        public static final String TAG = "rss";

        public static final String DATABASE_NAME = "RSSDB22";
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


        public static final String PREFERENCES_FILE = "my_settings_3";
        public static final String PREFERENCES_TIME_REFRESH = "refresher";
        public static final String PREFERENCES_NOTIFICATION = "notification";

        public final static String REFRESH_TIME = "com.example.newsAggregator.REFRESH_TIME";

        public static final int SOURCE_ALREADY_ADDED = 1;
        public static final int SOURCE_ADDED = 2;

    }
}
