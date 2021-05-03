package com.example.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class Activity2 extends AppCompatActivity {
    private Button search;
    private Button history;
    private SearchView searchView;
    private String searchText;
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    //date stuff
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    //Billboard stuff
    private static AsyncHttpClient client = new AsyncHttpClient();

    private String header1= "x-rapidapi-key";
    private String valueHeader1= "c6a7a1eb1amsh5b29e5a4cdfbcc7p1363bejsnbd59f45cc09c";
    private String header2="x-rapidapi-host";
    private String valueHeader2="billboard-api2.p.rapidapi.com";

    private ArrayList<String>Top100;
    private String urlTop100 = "https://billboard-api2.p.rapidapi.com/artist-100?";
    private ArrayList<String>Top200;
    private String urlTop200="https://billboard-api2.p.rapidapi.com/billboard-200?";
    private ArrayList<String>top100Artist;
    private String urlHot100="https://billboard-api2.p.rapidapi.com/hot-100?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        databaseHelper = new DatabaseHelper(Activity2.this, "history.db",null, 1);
        recyclerView= findViewById(R.id.recyclerViewBB);
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());

       // Top100= getTop10(urlHot100, date ,Top100);
       // Top200= getTop10(urlTop200, date ,Top200);
        //top100Artist= getTop10(urlTop100, date ,top100Artist);
        //setBillboardContent(Top100, Top200,top100Artist);

        search = findViewById(R.id.button_search);
        history = findViewById(R.id.button_history);
        searchView = findViewById(R.id.searchView);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Activity2.this, Activity3.class);
                searchText=searchView.getQuery().toString();
                boolean yes=false;
                long rowID =databaseHelper.addHistory(new History(searchText));

                String artistID = getArtistID(searchText);
                if(!getArtistID(searchText).equals("")){
                    Log.d("artistID:", artistID);
                    intent.putExtra("artistID", artistID);
                    yes=true;
                }
                if(yes){
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Activity2.this);
                    builder1.setMessage("Spotiboard seems to not have that Artist information readily available. Please check your spelling or try a different Artist.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Understand",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Activity2.this, historyActivity.class);
                startActivity(intent);
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

    public ArrayList<String> getTop10(String url, String date, ArrayList<String> chart){
        client.addHeader("accept", "application/json");
        client.addHeader(header1, valueHeader1);
        client.addHeader(header2, valueHeader2);
        String temp= date.toString();
        String d= temp.replace('/','-');

        url=url+ "date="+d+"&range=1-10";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    JSONObject contents= new JSONObject((new String(responseBody)));

                       Log.d("Contents: ", contents.toString());
                       JSONObject cont= contents.getJSONObject("content");
                       int count=1;

                       while(count<11){
                           String temp = String.valueOf(count);
                           JSONObject t= cont.getJSONObject(temp);
                           String toAdd = t.getString("artist");
                           Log.d("Artist:", toAdd);
                           chart.add(toAdd);
                           count+=1;
                       }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("api error", new String(responseBody));
            }
        });
        return chart;
    }

//    public void setBillboardContent(ArrayList<String> one, ArrayList<String> two,
//                                    ArrayList<String> three){
//
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//
//        RecyclerView myList = (RecyclerView) findViewById(R.id.recyclerViewBB);
//        myList.setLayoutManager(layoutManager);
//
//        BillboardAdapter billboardAdapter1= new BillboardAdapter(one, this);
//        //BillboardAdapter billboardAdapter2= new BillboardAdapter(two, this);
//       // BillboardAdapter billboardAdapter3= new BillboardAdapter(three, this);
//
//        myList.setAdapter(billboardAdapter1);
//       // myList.setAdapter(billboardAdapter2);
//       // myList.setAdapter(billboardAdapter3);
//
//        myList.setLayoutManager(new LinearLayoutManager(Activity2.this));
//
//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Activity2.this, DividerItemDecoration.VERTICAL);
//        myList.addItemDecoration(itemDecoration);
//    }

}