package com.example.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class fragment_searchBillboard extends Fragment {

    private View view;
    private EditText date;
    private TextView one;
    private TextView two;
    private TextView three;

    private SharedPreferences sharedPreferences;
    private String artist_name;
    private String urlTop100 = "https://billboard-api2.p.rapidapi.com/artist-100?";
    private String urlTop200 = "https://billboard-api2.p.rapidapi.com/billboard-200?";
    private String urlHot100 = "https://billboard-api2.p.rapidapi.com/hot-100?";

    private String header1= "x-rapidapi-key";
    private String valueHeader1 = "bef3c908bamsh7fbf3f00567f4afp1008c1jsnd358694fdc79";
    private String header2="x-rapidapi-host";
    private String valueHeader2="billboard-api2.p.rapidapi.com";
    private Button buttonSearch;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.item_billboard,container, false);

        sharedPreferences = getActivity().getSharedPreferences("SPOTIFY", 0);
        artist_name = sharedPreferences.getString("artist2", "");

        date= view.findViewById(R.id.editTextDate_1);
        one=view.findViewById(R.id.textEditInformation);
        two=view.findViewById(R.id.textViewInformation2);
        three=view.findViewById(R.id.textViewInformation3);
        buttonSearch = view.findViewById(R.id.buttonsearch);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!date.getText().equals("")){

                    String date1= date.getText().toString();
                    searchChart1(urlTop100, date1, artist_name);
                    searchChart2(urlTop200, date1, artist_name);
                    searchChart3(urlHot100, date1, artist_name);

                }
            }
        });

        return view;
    }

    public void searchChart1(String url, String date, String artist_name) {
        client.addHeader("accept", "application/json");
        client.addHeader(header1, valueHeader1);
        client.addHeader(header2, valueHeader2);

        String temp = date;
        String d = temp.replace('/', '-');

        url = url + "date=" + d + "&range=1-10";
        ArrayList<String> infoChart1 = new ArrayList<>();
        Log.d("URL ", url);

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject contents = new JSONObject((new String(responseBody)));
                    JSONObject cont = contents.getJSONObject("content");

                    int count = 1;
                    while (count < 11) {
                        String temp = String.valueOf(count);
                        JSONObject t = cont.getJSONObject(temp);
                        String toTest = t.getString("artist");
                        Log.d("TESTING:", toTest);
                        if (toTest.equals(artist_name)) {
                            infoChart1.add(t.getString("rank"));
                            Log.d("TEST:", t.getString("rank"));
                            infoChart1.add(t.getString("weeks on chart"));
                            Log.d("TEST:", t.getString("weeks on chart"));
                            infoChart1.add(t.getString("last week"));
                            Log.d("TEST:", t.getString("last week"));
                            break;
                        }
                        count++;
                    }
                    if(infoChart1.size()>0){
                        //Top 100 artist chart
                        String Val= "The artist " + artist_name +
                                " has appeared on the Artist Top 100 chart ton the week of: " + date +
                                " It is ranked: "
                                + infoChart1.get(0) + " on the chart with "
                                + infoChart1.get(1) + " weeks on the charts. The week before " + date + " they were at the position "
                                + infoChart1.get(2);
                        one.setText(Val);
                    }
                    else{
                        one.setText( artist_name + " did not appear on the Artist Top 100 chart this week.");
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                one.setText("Failed to connect to the API ");
            }
        });
    }

    public void searchChart2(String url, String date, String artist_name) {
        client.addHeader("accept", "application/json");
        client.addHeader(header1, valueHeader1);
        client.addHeader(header2, valueHeader2);

        String temp = date;
        String d = temp.replace('/', '-');

        url = url + "date=" + d + "&range=1-10";
        ArrayList<String> infoChart1 = new ArrayList<>();
        Log.d("URL ", url);

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject contents = new JSONObject((new String(responseBody)));
                    JSONObject cont = contents.getJSONObject("content");

                    int count = 1;
                    while (count < 11) {
                        String temp = String.valueOf(count);
                        JSONObject t = cont.getJSONObject(temp);
                        String toTest = t.getString("artist");
                        Log.d("TESTING:", toTest);
                        if (toTest.equals(artist_name)) {
                            infoChart1.add(t.getString("rank"));
                            infoChart1.add(t.getString("album"));
                            infoChart1.add(t.getString("weeks on chart"));
                            infoChart1.add(t.getString("last week"));


                            break;
                        }
                        count++;
                    }

                    if(infoChart1.size()>0){
                        //Top 100 artist chart
                        String Val= "The artist " + artist_name +
                                " has appeared on the Billboard Top 100 chart the week of: " + date + ". "+
                                "It is ranked: "+
                                infoChart1.get(0) + " with the album: " +
                                infoChart1.get(1)+
                                " on the chart with " +
                                infoChart1.get(2) +
                                " weeks on the charts. The week before they were at the position "  +
                                infoChart1.get(3);
                        two.setText(Val);
                    }
                    else{
                        two.setText( artist_name + " did not appear on the Billboard Top 200 chart this week.");
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                two.setText("Failed to connect to the API ");
            }
        });
    }

    public void searchChart3(String url, String date, String artist_name) {
        client.addHeader("accept", "application/json");
        client.addHeader(header1, valueHeader1);
        client.addHeader(header2, valueHeader2);

        String temp = date;
        String d = temp.replace('/', '-');

        url = url + "date=" + d + "&range=1-10";
        ArrayList<String> infoChart1 = new ArrayList<>();
        Log.d("URL ", url);

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject contents = new JSONObject((new String(responseBody)));
                    JSONObject cont = contents.getJSONObject("content");

                    int count = 1;
                    while (count < 11) {
                        String temp = String.valueOf(count);
                        JSONObject t = cont.getJSONObject(temp);
                        String toTest = t.getString("artist");
                        Log.d("TESTING:", toTest);
                        if (toTest.equals(artist_name)) {
                            infoChart1.add(t.getString("rank"));
                            infoChart1.add(t.getString("title"));
                            infoChart1.add(t.getString("weeks on chart"));
                            infoChart1.add(t.getString("last week"));
                            break;
                        }
                        count++;
                    }

                    if(infoChart1.size()>0){
                        //Top 100 artist chart
                        String Val= "The artist " + artist_name +
                                " has appeared on the Hot 100 chart on the week of: " + date+ "." +
                                " It is ranked: "+
                                infoChart1.get(0) + "with the song: " +
                                infoChart1.get(1)+
                                " on the chart with " +
                                infoChart1.get(2) +
                                " weeks on the charts. The week before they were at the position "  +
                                infoChart1.get(3);
                        three.setText(Val);
                    }
                    else{
                        three.setText( artist_name + " did not appear on the Hot 100 chart this week.");
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                three.setText("Failed to connect to the API ");
            }
        });
    }
}