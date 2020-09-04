package io.qxtno.showepisodegenerator;

import android.provider.BaseColumns;

public class ShowContract {

    private ShowContract(){}

    public static final class ShowEntry implements BaseColumns{
        public static final String TABLE_NAME = "showList";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SEASONS = "seasons";
        public static final String COLUMN_FAV = "fav";
        public static final String COLUMN_CUSTOM = "custom";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
