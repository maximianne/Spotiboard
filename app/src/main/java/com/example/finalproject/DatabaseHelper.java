package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.example.finalproject.DatabaseContract;
import com.example.finalproject.History;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context, @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStatement= "CREATE TABLE " + DatabaseContract.UserEntry.TABLE_NAME + "(" +
                DatabaseContract.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.UserEntry.COLUMN_SEARCH + " TEXT)";

        SQLiteStatement statement= db.compileStatement(sqlStatement);
        statement.execute();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this is going to be called when the version number updates

    }
    public long addHistory(History history){

        SQLiteDatabase database = getWritableDatabase();
        String sqlQuery= "INSERT INTO " + DatabaseContract.UserEntry.TABLE_NAME+
                "(" + DatabaseContract.UserEntry.COLUMN_SEARCH +") VALUES (?)";

        SQLiteStatement statement= database.compileStatement(sqlQuery);
        String search= history.getSearchText();
        statement.bindString(1,search);

        long rowID= statement.executeInsert();
        database.close();
        return rowID;
    }

    public List<History> getAllHistory(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<History> histories = new ArrayList<>();

        String sqlQuery = "SELECT * FROM " + DatabaseContract.UserEntry.TABLE_NAME;
        Cursor resultSet= sqLiteDatabase.rawQuery(sqlQuery, null);

        if(resultSet.moveToFirst()){
            do{
                String historyText= resultSet.getString(1);
//                String historyDate = resultSet.getString(2);
//                String historyTime= resultSet.getString(3);
                History history= new History(historyText);
                histories.add(history);
                long history_id = resultSet.getLong(0);
                history.setId(history_id);

            }while(resultSet.moveToNext());
        }
        resultSet.close();
        sqLiteDatabase.close();
        return histories;
    }

    public int deleteUser(History history){
        SQLiteDatabase database = getWritableDatabase(); //we are changing it

        String sqlQuery= "DELETE FROM " +DatabaseContract.UserEntry.TABLE_NAME +
                " where " + DatabaseContract.UserEntry.COLUMN_ID + " = ?";

        SQLiteStatement statement = database.compileStatement(sqlQuery);
        statement.bindLong(1,history.getId());

        int numRows =statement.executeUpdateDelete();
        database.close();
        return numRows;
    }
}