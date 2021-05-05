package com.example.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class historyActivity extends AppCompatActivity {

    private RecyclerView recyclerView_history;
    private DatabaseHelper databaseHelper;
    private List<History> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        recyclerView_history=findViewById(R.id.recyclerView_history);
        databaseHelper = new DatabaseHelper(historyActivity.this, "history.db",null, 1);

        histories=databaseHelper.getAllHistory();

        HistoryAdapter adapter = new HistoryAdapter(histories, databaseHelper, historyActivity.this);
        recyclerView_history.setAdapter(adapter);
        recyclerView_history.setLayoutManager(new LinearLayoutManager(historyActivity.this));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(historyActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView_history.addItemDecoration(itemDecoration);

    }

}