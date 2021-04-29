package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;

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
    protected ArrayList<String> urls;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);
        Intent intent=getIntent();
        sharedPreferences=this.getSharedPreferences("SPOTIFY", 0);
        artistID=intent.getStringExtra("artistID");

        databaseHelper = new DatabaseHelper(Activity3.this, "history.db",
                null, 1);

        topTracks= findViewById(R.id.button_frag1);
        billboard=findViewById(R.id.button_frag2);
        howTo=findViewById(R.id.button_frag3);

        artistName= findViewById(R.id.textView_artistname);
        setArtistN(artistID);

        urls=artistImageURL(artistID);
        Bundle bundle1=new Bundle();
        bundle1.putStringArrayList("url", urls);
        fragment_image frag= new fragment_image();
        frag.setArguments(bundle1);

        artist_toptracks=getArtistTTracks(artistID);
        Bundle bundle=new Bundle();
        bundle.putStringArrayList("topTracks", artist_toptracks);

        fragment_topTracks frag1 = new fragment_topTracks();
        frag1.setArguments(bundle);

        topTracks.setOnClickListener(v -> loadFragment(frag1));
        billboard.setOnClickListener(v-> loadFragment(frag));

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

    public ArrayList<String> artistImageURL(String artistID){
        SpotifyApi api= getSpotifyService();
        SpotifyService spotify = api.getService();
        ArrayList<String> im= new ArrayList<>();
        spotify.getArtist(artistID, new Callback<Artist>() {
            @Override
            public void success(Artist artist, retrofit.client.Response response) {
                for(int i=0;i<artist.images.size();i++){
                    im.add(artist.images.get(i).url);
                }
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
        return im;
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_act3,fragment);
        fragmentTransaction.commit();
    }
}
