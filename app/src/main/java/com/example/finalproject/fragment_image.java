package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class fragment_image extends Fragment {

    private View view;
    protected ImageView imageView;
    protected ArrayList<String> url;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_image,container, false);

        imageView=view.findViewById(R.id.imageView2);
        url= getArguments().getStringArrayList("url");

        Picasso.get().load(url.get(0)).into(imageView);

        return view;
    }


}
