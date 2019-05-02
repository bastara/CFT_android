package com.example.newsaggregator.db;

import android.provider.BaseColumns;

public class NewsContract {

    private NewsContract() {
    }

    public static final class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME1 = "news";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK1 = "link";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_ISSHOW = "isShow";
        public static final String COLUMN_PUBDATE = "pubDate";

        public static final String TABLE_NAME2 = "tableOfSite";
        public static final String COLUMN_LINK2 = "link";
    }
}
