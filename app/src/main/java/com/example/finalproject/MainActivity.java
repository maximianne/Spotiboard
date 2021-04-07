package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView title;
    private Button start;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title=findViewById(R.id.textView);
        start=findViewById(R.id.button);
        image=findViewById(R.id.imageView);
        start.setText("Start");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNextActivity(v);
            }
        });
    }
    public void launchNextActivity(View v){
        Intent intent = new Intent(MainActivity.this, Activity1.class);
        startActivity(intent);
    }
}
