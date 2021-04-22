package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity3 extends AppCompatActivity {

    private TextView artistName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);

        Intent intent=getIntent();

        artistName=findViewById(R.id.textView_artistname);
        artistName.setText(intent.getStringExtra("artistName"));


    }
}
