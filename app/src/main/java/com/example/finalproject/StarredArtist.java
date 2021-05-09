package com.example.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StarredArtist extends AppCompatActivity {

    private TextView title;
    private Button goBack;
    private LinearLayout artistsList;

    private FirebaseDatabase db;
    private DatabaseReference refer;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starred);

        title = findViewById(R.id.textView7);
        goBack = findViewById(R.id.button4);
        artistsList = findViewById(R.id.linearLayout);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarredArtist.this, Activity2.class);
                startActivity(intent);
            }
        });

    starredArtists();

    }

    public void starredArtists() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // added to code
        String uid = user.getUid(); // pulls the UID
        db = FirebaseDatabase.getInstance();
        refer = db.getReference();
        refer.child(uid).child("favoriteArtists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<String> starredArtists = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String artistsNames = String.valueOf(dataSnapshot.getKey());
                    Log.d("valuesArray", artistsNames);
                    String value = String.valueOf(dataSnapshot.getValue());
                    Log.d("valuesArray", value);
                    if(value.equals("true")) {
                        starredArtists.add(artistsNames);
                    }
                }
                Log.d("valuesArray", String.valueOf(starredArtists));
                TextView[] artists = new TextView[starredArtists.size()];
                TextView temp = null;

                for (int i = 0; i < starredArtists.size(); i++){

                    temp = new TextView(getApplicationContext());
                    temp.setText(starredArtists.get(i)); //arbitrary task
                    temp.setTextSize(24);
                    temp.setTextColor(Color.BLACK);
                    // add the textview to the linearlayout
                    TextView finalTemp = temp;
                    temp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        Intent intent= new Intent(StarredArtist.this, Activity3.class);
                        String temp2 = finalTemp.getText().toString();

                        String artistID = getArtistID(temp2);
                            Log.d("artistID:", artistID);
                            intent.putExtra("artistID", artistID);
                            intent.putExtra("artistName", temp2);

                            startActivity(intent);

                        }
                    });
                    artistsList.addView(temp);
                    artists[i] = temp;
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private String getArtistID(String artist){
        String id="";
        try {
            JSONObject SpotJSON = new JSONObject(loadJSONFromAsset("spotify.json"));
            JSONArray spotArray = SpotJSON.getJSONArray("spotifyArtists");

            for(int i = 0; i< spotArray.length(); i++){
                JSONObject artObject = spotArray.getJSONObject(i);
                if(artObject.getString("name").equalsIgnoreCase(artist)){
                    id= artObject.getString("id");
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }
    private String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
