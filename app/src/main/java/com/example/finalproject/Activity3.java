package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);
        Intent intent=getIntent();
        sharedPreferences=this.getSharedPreferences("SPOTIFY", 0);
        artistID=intent.getStringExtra("artistID");

        artistName= findViewById(R.id.textView_artistname);
        recyclerView= findViewById(R.id.recyclerView_act3);
        setArtistN(artistID);
        getArtistTTracks(artistID);
    }

    public void getArtistTTracks(String artistID){
        SpotifyApi api= getSpotifyService();
        SpotifyService spotify = api.getService();
        spotify.getArtistTopTrack(artistID, "US", new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, retrofit.client.Response response) {
                //Log.d("TRACKS", tracks.tracks.toString());
                ArrayList<String> toAdd=new ArrayList<>();
                for(int i=0;i<tracks.tracks.size();i++) {
                    toAdd.add(tracks.tracks.get(i).name);
                    Log.d("TOP TRACKS", tracks.tracks.get(i).name);
                }

            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
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
}
