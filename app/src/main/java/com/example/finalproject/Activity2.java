package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Activity2 extends AppCompatActivity {
    private Button search;
    private Button history;
    private DatabaseHelper databaseHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        //file extension for databse file endes in db
        databaseHelper = new DatabaseHelper(Activity2.this, "histories.db",
                null, 1);

        search = findViewById(R.id.button_search);
        history = findViewById(R.id.button_history);
        searchView = findViewById(R.id.searchView);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searched =searchView.getQuery().toString();
                if(searched.isEmpty()){
                    Toast.makeText(Activity2.this, "missing feilds", Toast.LENGTH_SHORT).show();;
                }
                else {
                    long rowID=databaseHelper.addHistory(new History(searched));
                    Toast.makeText(Activity2.this, "RowId: " + rowID, Toast.LENGTH_SHORT).show();
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNextActivity(v);
            }
        });
    }
        public void launchNextActivity (View v){
            Intent intent = new Intent(Activity2.this, historyActivity.class);
            startActivity(intent);
        }

}
