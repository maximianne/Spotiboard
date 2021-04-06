package com.example.finalproject;

import android.provider.BaseColumns;

public class DatabaseContract {
    //define name and columns

    //to prevent someone from accidently creating a DatabaseContract object
    //we will make the constructor private
    private DatabaseContract(){
    }

    public static class UserEntry implements BaseColumns{
        //provide names of the tables coulmns
        public static final String TABLE_NAME="history_table";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_SEARCH="searchText";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_TIME ="time";
    }

}
