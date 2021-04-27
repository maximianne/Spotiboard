package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class fragment_topTracks extends Fragment {
    private View view;
    private ArrayList<TextView> textboxes;
    private ArrayList<String> topTracks;
    private String artistN;
    private TextView v,v1,v2,v3,v4,v5,v6, v7, v8, v9, v10;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_toptracks,container, false);
        topTracks= getArguments().getStringArrayList("topTracks");

        v= view.findViewById(R.id.textView_ttTitle);
        v.setText("Top Tracks On Spotify");

        v1= view.findViewById(R.id.textView_tt1);
        v1.setText(topTracks.get(0));

        v2= view.findViewById(R.id.textView_tt2);
        v2.setText(topTracks.get(1));

        v3= view.findViewById(R.id.textView_tt3);
        v3.setText(topTracks.get(2));

        v4= view.findViewById(R.id.textView_tt4);
        v4.setText(topTracks.get(3));

        v5= view.findViewById(R.id.textView_tt5);
        v5.setText(topTracks.get(4));

        v6= view.findViewById(R.id.textView_tt6);
        v6.setText(topTracks.get(5));

        v7= view.findViewById(R.id.textView_tt7);
        v7.setText(topTracks.get(6));

        v8= view.findViewById(R.id.textView_tt8);
        v8.setText(topTracks.get(7));

        v9= view.findViewById(R.id.textView_tt9);
        v9.setText(topTracks.get(8));

        v10= view.findViewById(R.id.textView_tt10);
        v10.setText(topTracks.get(9));

        return view;
    }

}
