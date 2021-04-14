package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder>{

    private ArrayList<billboardArtistInfo> billboardArtistInfos;

    public ChartAdapter(ArrayList<billboardArtistInfo> billboardArtistInfos){
        this.billboardArtistInfos=billboardArtistInfos;
    }

    @NonNull
    @Override
    public ChartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //inflate the custom layout
        View item = inflater.inflate(R.layout.item_top100a2, parent, false);
        //return a new ViewHolder
        ViewHolder viewHolder = new ViewHolder(item);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ChartAdapter.ViewHolder holder, int position) {
//        billboardArtistInfo curr= billboardArtistInfos.get(position);
//        holder.
//                .setText(curr.getName());
//        holder.textView_LocationType.setText("Type: " + curr.getType());
//        holder.textView_LocationDimension.setText("Dimension: " + curr.getDimension());
    }

    @Override
    public int getItemCount() {
        return billboardArtistInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView artist1name;
        TextView artist2name;
        TextView artist3name;

        TextView topTrack1;
        TextView topTrack2;
        TextView topTrack3;

        TextView streams1;
        TextView streams2;
        TextView streams3;

        public ViewHolder(View itemView){
            super(itemView);
            //Context context = itemView.getContext();
            artist1name=itemView.findViewById(R.id.textView_artist1name);
            artist2name=itemView.findViewById(R.id.textView_artist2name);
            artist3name=itemView.findViewById(R.id.textView_artist3name);

            topTrack1=itemView.findViewById(R.id.textView_topSong1);
            topTrack2=itemView.findViewById(R.id.textView_topSong2);
            topTrack3=itemView.findViewById(R.id.textView_topSong3);

            streams1=itemView.findViewById(R.id.textView_streams1);
            streams2=itemView.findViewById(R.id.textView_streams2);
            streams3=itemView.findViewById(R.id.textView_stream3);

        }
    }
}
