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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);
        Intent intent=getIntent();
        sharedPreferences=this.getSharedPreferences("SPOTIFY", 0);
        artistID=intent.getStringExtra("artistID");

        topTracks= findViewById(R.id.button_frag1);
        billboard=findViewById(R.id.button_frag2);
        howTo=findViewById(R.id.button_frag3);

        artistName= findViewById(R.id.textView_artistname);
        setArtistN(artistID);

        //TRYING TO LOAD IMAGE AS DEFAULT
        urls=artistImageURL(artistID);

        Bundle bundle2=new Bundle();
        bundle2.putStringArrayList("url", urls);

        fragment_topTracks frag= new fragment_topTracks();
        frag.setArguments(bundle2);

        //loadFragment(frag);


        artist_toptracks=getArtistTTracks(artistID);

        Bundle bundle=new Bundle();
        bundle.putStringArrayList("topTracks", artist_toptracks);

        fragment_topTracks frag1 = new fragment_topTracks();
        frag1.setArguments(bundle);

        topTracks.setOnClickListener(v -> loadFragment(frag1));

    }

    public ArrayList<String> getArtistTTracks(String artistID){
        SpotifyApi api= getSpotifyService();
        SpotifyService spotify = api.getService();
        ArrayList<String> toAdd=new ArrayList<>();
        spotify.getArtistTopTrack(artistID, "US", new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, retrofit.client.Response response) {
                //Log.d("TRACKS", tracks.tracks.toString());
                for(int i=0;i<tracks.tracks.size();i++) {
                    toAdd.add(tracks.tracks.get(i).name);
                    Log.d("TOP TRACKS", tracks.tracks.get(i).name);
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
                    Log.d("imagesurl", im.toString());
                }
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
        return im;
    }

    public void loadFragment(Fragment fragment){
        //create a fragment manager
        FragmentManager fragmentManager=getSupportFragmentManager();

        //create a fragment transaction to begin the tranaction to replace the fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //repalcing the place holder = fragment container view eith the fragment that is passed as parameter
        fragmentTransaction.replace(R.id.fragment_act3,fragment);
        fragmentTransaction.commit();
    }


}
