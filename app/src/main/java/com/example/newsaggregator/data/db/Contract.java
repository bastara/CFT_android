package com.example.newsaggregator.data.db;

public class Contract {

    private Contract() {
    }

    public static final class Entry {
        public static final String TAG = "rss";

        static final String DATABASE_NAME = "RSSDB22";
        static final int DATABASE_VERSION = 1;

        public static final String TABLE_NEWS = "news";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK_NEWS = "link";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_ISSHOW = "isShow";
        public static final String COLUMN_PUBDATE = "pubDate";
        public static final String COLUMN_URL = "url";

        public static final String TABLE_SITES = "tableOfSite";
    }
}
