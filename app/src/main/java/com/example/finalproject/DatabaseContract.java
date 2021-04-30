package com.example.finalproject;

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract(){
    }
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "history_table";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "searched";
    }
}