package com.example.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

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

public class Activity2 extends AppCompatActivity {
    private Button search;
    private Button history;
    private SearchView searchView;
    private String searchText;

    private SharedPreferences sharedPreferences;
    private String urltt1 ="https://api.spotify.com/v1/artists/";
    private String urltt2="/top-tracks?market=US/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        sharedPreferences=this.getSharedPreferences("SPOTIFY", 0);
        search = findViewById(R.id.button_search);
        history = findViewById(R.id.button_history);
        searchView = findViewById(R.id.searchView);

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

        Log.d("THE TOKEN:", token);

        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        search.setOnClickListener(new View.OnClickListener() { //Maxie's code
            @Override
            public void onClick(View v) {
                searchText=searchView.getQuery().toString();
                String artistID = getArtistID(searchText);
                    if(!getArtistID(searchText).equals("")){

                        Log.d("artistID:", artistID);
                        spotify.getArtistTopTrack(artistID, "US", new Callback<Tracks>() {
                            @Override
                            public void success(Tracks tracks, retrofit.client.Response response) {
                                Log.d("Tracks success",tracks.tracks.toString());
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("Tracks failure", error.toString());
                            }
                        });

                        spotify.getArtist(artistID, new Callback<Artist>() {
                            @Override
                            public void success(Artist artist, retrofit.client.Response response) {
                                Log.d("Album success", artist.name);
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("Album failure", error.toString());
                            }
                        });
                }
                else{
                    Toast.makeText(Activity2.this, "Artist does not exist", Toast.LENGTH_LONG).show();
               }
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

                if(artObject.getString("name").equals(artist)){
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