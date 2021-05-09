package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Tracks;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit.Callback;
import retrofit.RetrofitError;

public class Activity3 extends AppCompatActivity {

    private TextView artistName;
    private SharedPreferences sharedPreferences;
    private String artistID;
    private Button topTracks;
    private Button billboard;
    private Button howTo;
    protected ArrayList<String> artist_toptracks;

    private Switch favorite;
    private FirebaseDatabase db;
    private DatabaseReference refer;
    private String ArtistName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);
        Intent intent = getIntent();
        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        artistID = intent.getStringExtra("artistID");

        topTracks = findViewById(R.id.button_frag1);
        billboard = findViewById(R.id.button_frag2);
        howTo = findViewById(R.id.button_frag3);
        favorite = findViewById(R.id.switch1);

        artistName = findViewById(R.id.textView_artistname);
        setArtistN(artistID);

        artistImageURL(artistID);

        artist_toptracks = getArtistTTracks(artistID);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("topTracks", artist_toptracks);

        fragment_topTracks frag1 = new fragment_topTracks();
        frag1.setArguments(bundle);

        topTracks.setOnClickListener(v -> loadFragment(frag1));

        fragment_searchBillboard frag2=new fragment_searchBillboard();
        howTo.setOnClickListener(v-> loadFragment(frag2));
        fragment_billboardInfo frag3= new fragment_billboardInfo();
        billboard.setOnClickListener(v-> loadFragment(frag3));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // added to code
        String uid = user.getUid(); // pulls the UID
        db = FirebaseDatabase.getInstance();
        refer = db.getReference();

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = artistName.getText().toString();
                Log.e("text", text);
                refer.child(uid).child("favoriteArtists").child(text).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        String val = String.valueOf(task.getResult().getValue());
                        Log.d("values",val);
                            if(val.equals("true")){
                                refer.child(uid).child("favoriteArtists").child(text).setValue("false");
                            }
                            else{
                                refer.child(uid).child("favoriteArtists").child(text).setValue("true");
                            }
                    }
                });
            }
        });
    }

    public ArrayList<String> getArtistTTracks(String artistID){
        SpotifyApi api= getSpotifyService();
        SpotifyService spotify = api.getService();
        ArrayList<String> toAdd=new ArrayList<>();
        spotify.getArtistTopTrack(artistID, "US", new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, retrofit.client.Response response) {
                for(int i=0;i<tracks.tracks.size();i++) {
                    toAdd.add(tracks.tracks.get(i).name);
                    Log.d("TOP TRACKS", tracks.tracks.get(i).name);
                    Log.d("tracks:", toAdd.get(i));
                }
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
        return toAdd;
    }

    public void setArtistN(String artistID) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // added to code
        String uid = user.getUid(); // pulls the UID
        db = FirebaseDatabase.getInstance();
        refer = db.getReference();
        SpotifyApi api = getSpotifyService();
        SpotifyService spotify = api.getService();
        spotify.getArtist(artistID, new Callback<Artist>() {
            @Override
            public void success(Artist artist, retrofit.client.Response response) {
                Log.d("Artist success", artist.name);
                String artistN = artist.name;
                ArtistName = artist.name;
                artistName.setText(artistN);
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putString("artist2", artistN);
                editor.apply();
                // databaseHelper.addHistory(new History(artistN));
                refer.child(uid).child("favoriteArtists").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        ArrayList<String> artistsFav = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String value = String.valueOf(dataSnapshot.getKey());
                            artistsFav.add(value);
                        }
                        for (int i = 0; i < artistsFav.size(); i++) {
                            Log.d("valuesArray", String.valueOf((artistsFav)));
                            if (!artistsFav.contains(artistN)) {
                                refer.child(uid).child("favoriteArtists").child(artistN).setValue("false");
                            }
                        }
                        refer.child(uid).child("favoriteArtists").child(artistN).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                String val = String.valueOf(task.getResult().getValue());
                                favorite.setChecked(Boolean.parseBoolean(val));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public SpotifyApi getSpotifyService(){
        SpotifyApi api = new SpotifyApi();
        String token=sharedPreferences.getString("token", "");
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        api.setAccessToken(token);
        return api;
    }

    public void artistImageURL(String artistID){
        SpotifyApi api= getSpotifyService();
        SpotifyService spotify = api.getService();
        ArrayList<String> im= new ArrayList<>();
        spotify.getArtist(artistID, new Callback<Artist>() {
            @Override
            public void success(Artist artist, retrofit.client.Response response) {
                for(int i=0;i<artist.images.size();i++){
                    im.add(artist.images.get(i).url);
                }
                String url = im.get(0);
                Bundle bundle1=new Bundle();
                bundle1.putString("url", url);
                fragment_image frag= new fragment_image();
                frag.setArguments(bundle1);
                loadFragment(frag);
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_act3,fragment);
        fragmentTransaction.commit();
    }
}