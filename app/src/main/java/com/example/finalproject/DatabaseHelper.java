package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlStatement = "CREATE TABLE " + DatabaseContract.UserEntry.TABLE_NAME + "(" +
                DatabaseContract.UserEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.UserEntry.COLUMN_NAME + " TEXT)";
        SQLiteStatement statement = db.compileStatement(sqlStatement);
        statement.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long addHistory(History user) {

        SQLiteDatabase database = this.getWritableDatabase();
        String sqlQuery = "INSERT INTO " + DatabaseContract.UserEntry.TABLE_NAME +
                "(" + DatabaseContract.UserEntry.COLUMN_NAME + ") Values (?)";
        SQLiteStatement statement = database.compileStatement(sqlQuery);

        String name = user.getSearched();
        statement.bindString(1, name);


        long rowId = statement.executeInsert();
        database.close();
        return rowId;
    }

    // select
    // query all the user from the table
    public List<History> getAllHistory() {
        //SELECT *  FROM table_name;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<History> histories = new ArrayList<>();

        // cursor -> a set of results
        String sqlQuery = "SELECT * FROM " + DatabaseContract.UserEntry.TABLE_NAME;
        Cursor resultSet = sqLiteDatabase.rawQuery(sqlQuery, null);

        // resultSet.moveToFirst() -> false -> you don't have any results
        if (resultSet.moveToFirst()) {
            // loop through the entire resultSet

            do {
                // get each row and save it into a User object
                // add the object to the list
                String user_name = resultSet.getString(1);
                History user = new History(user_name);
                long user_id = resultSet.getLong(0);
                user.setId(user_id);
                histories.add(user);

            } while (resultSet.moveToNext());
        }
        resultSet.close();
        sqLiteDatabase.close();
        return histories;
    }

    public int deleteHistory(History user){
        SQLiteDatabase database = this.getWritableDatabase();
        String sqlQuery = "DELETE FROM " + DatabaseContract.UserEntry.TABLE_NAME + " where "
                + DatabaseContract.UserEntry.COLUMN_ID + " =?";
        SQLiteStatement statement = database.compileStatement(sqlQuery);
        statement.bindLong(1, user.getId());

        int numRows = statement.executeUpdateDelete();
        database.close();
        return numRows;
    }

}