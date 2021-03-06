package com.example.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import safety.com.br.android_shake_detector.core.ShakeDetector;

public class Activity2 extends AppCompatActivity {
    private Button search;
    private Button history;
    private Button starredArtist;
    private SearchView searchView;
    private String searchText;
    private DatabaseHelper databaseHelper;

    //date stuff
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    //Billboard stuff
    private static AsyncHttpClient client = new AsyncHttpClient();
    private String header1= "x-rapidapi-key";
    private String valueHeader1= "9afcd10608msh0a657fbc271aadap198f29jsnfc9e2162caec";
    private String header2="x-rapidapi-host";
    private String valueHeader2="billboard-api2.p.rapidapi.com";
    private String urlTop100 = "https://billboard-api2.p.rapidapi.com/artist-100?";
    private String urlTop200="https://billboard-api2.p.rapidapi.com/billboard-200?";
    private String urlHot100="https://billboard-api2.p.rapidapi.com/hot-100?";

    private LinearLayout mainLayout;
    private HorizontalScrollView hsv;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;

    private String url1;
    private String url2;
    private String url3;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        databaseHelper = new DatabaseHelper(Activity2.this, "history.db",null, 1);

        //layout
        mainLayout= findViewById(R.id.linearLayoutA2);
        hsv = findViewById(R.id.hsv);
        layout1= findViewById(R.id.LinearLayout1);
        layout2=findViewById(R.id.LinearLayout2);
        layout3=findViewById(R.id.LinearLayout3);

        //date stuff
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = dateFormat.format(calendar.getTime());

      //  url1=getTop10Artist(urlTop100, date, layout1);
      //  url2=getHot100(urlHot100, date, layout2);
      //  url3=getBillboard200(urlTop200, date, layout3);

        search = findViewById(R.id.button_search);
        history = findViewById(R.id.button_history);
        searchView = findViewById(R.id.searchView);
        starredArtist = findViewById(R.id.button3);

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
                    intent.putExtra("artistName", searchText);
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

        starredArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, StarredArtist.class);
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
    public String getTop10Artist(String url, String date, LinearLayout lo){
        client.addHeader("accept", "application/json");
        client.addHeader(header1, valueHeader1);
        client.addHeader(header2, valueHeader2);
        String temp= date.toString();
        String d= temp.replace('/','-');

        url=url+ "date="+d+"&range=1-10";
        Log.d("REQUEST: ", url);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject contents= new JSONObject((new String(responseBody)));
                    Log.d("Contents: ", contents.toString());
                    JSONObject cont= contents.getJSONObject("content");
                    JSONObject titl= contents.getJSONObject("info");
                    String title= titl.getString("chart");
                    String date = titl.getString("date");
                    TextView textViewTitle = new TextView(getApplicationContext());
                    textViewTitle.setMinLines(2);
                    textViewTitle.setTextSize(18);
                    textViewTitle.setText(title);
                    textViewTitle.append(System.getProperty("line.separator"));
                    textViewTitle.append(date);

                    lo.addView(textViewTitle);

                    int count=1;
                    while(count<11){
                        String temp = String.valueOf(count);
                        TextView textView = new TextView(getApplicationContext());
                        JSONObject t= cont.getJSONObject(temp);
                        String toAdd = t.getString("artist");
                        textView.setText(count+ ": " + toAdd);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(Activity2.this, Activity3.class);
                                String Artist= null;
                                try {
                                    Artist = t.getString("artist");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                boolean yes=false;
                                long rowID =databaseHelper.addHistory(new History(Artist));

                                String artistID = getArtistID(Artist);
                                if(!getArtistID(Artist).equals("")){
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
                        lo.addView(textView);
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
        return url;
    }

    public String getHot100(String url, String date, LinearLayout lo){
        client.addHeader("accept", "application/json");
        client.addHeader(header1, valueHeader1);
        client.addHeader(header2, valueHeader2);
        String temp= date.toString();
        String d= temp.replace('/','-');

        url=url+ "date="+d+"&range=1-10";
        Log.d("REQUEST: ", url);

        String finalUrl = url;
        String finalUrl1 = url;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject contents= new JSONObject((new String(responseBody)));
                    Log.d("Contents: ", contents.toString());
                    JSONObject cont= contents.getJSONObject("content");
                    JSONObject titl= contents.getJSONObject("info");
                    String title= titl.getString("chart");
                    String date = titl.getString("date");
                    TextView textViewTitle = new TextView(getApplicationContext());
                    textViewTitle.setMinLines(2);
                    textViewTitle.setTextSize(18);
                    textViewTitle.setText(title);
                    textViewTitle.append(System.getProperty("line.separator"));
                    textViewTitle.append(date);

                    lo.addView(textViewTitle);
                    int count=1;
                    while(count<11){
                        String temp = String.valueOf(count);
                        TextView textView = new TextView(getApplicationContext());
                        textView.setMinLines(2);
                        JSONObject t= cont.getJSONObject(temp);
                        String toAdd = t.getString("title");
                        String toAdd2= t.getString("artist");

                        textView.setText(count+ ": " + "'" + toAdd+ "'");
                        textView.append(System.getProperty("line.separator"));
                        textView.append("by " + toAdd2);
                        lo.addView(textView);

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(Activity2.this, Activity3.class);
                                String Artist= null;
                                try {
                                    Artist = t.getString("artist");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                boolean yes=false;
                                long rowID =databaseHelper.addHistory(new History(Artist));

                                String artistID = getArtistID(Artist);
                                if(!getArtistID(Artist).equals("")){
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
        return url;
    }
    public String getBillboard200(String url, String date, LinearLayout lo){
        client.addHeader("accept", "application/json");
        client.addHeader(header1, valueHeader1);
        client.addHeader(header2, valueHeader2);
        String temp= date.toString();
        String d= temp.replace('/','-');

        url=url+ "date="+d+"&range=1-10";
        Log.d("REQUEST: ", url);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject contents= new JSONObject((new String(responseBody)));
                    Log.d("Contents: ", contents.toString());
                    JSONObject cont= contents.getJSONObject("content");
                    JSONObject titl= contents.getJSONObject("info");
                    String title= titl.getString("chart");
                    String date = titl.getString("date");
                    TextView textViewTitle = new TextView(getApplicationContext());
                    textViewTitle.setMinLines(2);
                    textViewTitle.setTextSize(18);
                    textViewTitle.setText(title);
                    textViewTitle.append(System.getProperty("line.separator"));
                    textViewTitle.append(date);

                    lo.addView(textViewTitle);
                    int count=1;
                    while(count<11){
                        String temp = String.valueOf(count);
                        TextView textView = new TextView(getApplicationContext());
                        textView.setMinLines(2);
                        JSONObject t= cont.getJSONObject(temp);
                        String toAdd = t.getString("album");
                        String toAdd2= t.getString("artist");

                        textView.setText(count+ ": " + "'" + toAdd+ "'");
                        textView.append(System.getProperty("line.separator"));
                        textView.append("by " + toAdd2);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(Activity2.this, Activity3.class);
                                String Artist= null;
                                try {
                                    Artist = t.getString("artist");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                boolean yes=false;
                                long rowID =databaseHelper.addHistory(new History(Artist));

                                String artistID = getArtistID(Artist);
                                if(!getArtistID(Artist).equals("")){
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
                        lo.addView(textView);
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
        return url;
    }
}