package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
       // billboard.setOnClickListener(v -> loadFragment(frag));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // added to code
        String uid = user.getUid(); // pulls the UID
        db = FirebaseDatabase.getInstance();
        refer = db.getReference();

        refer.child(uid).child("onSwitched").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                switchOn();
                String val = String.valueOf(task.getResult().getValue());
                favorite.setChecked(Boolean.parseBoolean(val));
            }
        });


        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                refer.child(uid).child("favoriteArtists").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    //ArrayList<String> artistsFav = new ArrayList<String>();
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.e("firebase", String.valueOf(task.getResult().getValue()));
                            String tempResult = String.valueOf(task.getResult().getValue());
                            String tempResult2 = "null";
                            String strArtistList = "";
                            Log.e("firebase",tempResult);
                            if (tempResult.equals(tempResult2)){ //when the starting value in the firebase favoriteArtists is null
                                ArrayList<String> artistsFav = new ArrayList<>();
                                //artistsFav.add(tempResult);
                                Log.e("values21", ArtistName);
                                artistsFav.add(ArtistName);
                                Log.e("values21", String.valueOf(artistsFav));
                                for (String s : artistsFav) {
                                    strArtistList += s + ",";
                                }
                            }
                            if (!tempResult.equals(tempResult2)) { //won't be added twice if found in database
                                ArrayList<String> artistsFav = new ArrayList<>(Arrays.asList(tempResult.split(",")));
                                int count = 0;
                                String value = ArtistName;
                                for(int i=0; i<artistsFav.size(); i++){
                                    if(!value.equals(artistsFav.get(i))) {
                                        count++;
                                        Log.e("count", String.valueOf(count));
                                        if (count == artistsFav.size()) {
                                            artistsFav.add(ArtistName);
                                        }
                                    }
                                }
                                Log.e("values", String.valueOf(artistsFav));
                                for (String s : artistsFav) {
                                    strArtistList += s + ",";
                                }
                            }
                            Log.e("values",strArtistList);
                            refer.child(uid).child("favoriteArtists").setValue(strArtistList);
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

    public void setArtistN(String artistID){
        SpotifyApi api= getSpotifyService();
        SpotifyService spotify = api.getService();
        spotify.getArtist(artistID, new Callback<Artist>() {
            @Override
            public void success(Artist artist, retrofit.client.Response response) {
                Log.d("Artist success", artist.name);
                String artistN=artist.name;
                ArtistName=artist.name;
                artistName.setText(artistN);
                // databaseHelper.addHistory(new History(artistN));
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

    public void switchOn(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // added to code
        String uid = user.getUid(); // pulls the UID
        db = FirebaseDatabase.getInstance();
        refer = db.getReference();
        refer.child(uid).child("favoriteArtists").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                refer.child(uid).child("onSwitched").setValue("null");
                String tempResult = String.valueOf(task.getResult().getValue());
                ArrayList<String> artistsFav = new ArrayList<>(Arrays.asList(tempResult.split(",")));
                int count = 0;
                String value = ArtistName;
                for (int i = 0; i < artistsFav.size(); i++) {
                    if (!value.equals(artistsFav.get(i))) {
                        count++;
                        Log.e("count", String.valueOf(count));
                        if (count == artistsFav.size()) {
                            refer.child(uid).child("onSwitched").setValue("false");
                        }
                        else {
                            refer.child(uid).child("onSwitched").setValue("true");
                        }
                    }
                }
            }
        });
    }
}