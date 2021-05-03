package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BillboardAdapter extends RecyclerView.Adapter<BillboardAdapter.ViewHolder> {

    private List<String> billboard;
    private Context context;

    public BillboardAdapter(List<String> billboard,
                          Context context){
        this.billboard=billboard;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);

        View userView = inflater.inflate(R.layout.chart_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(userView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String user= billboard.get(position);

        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));
        holder.billboardTitle.setText(billboard.get(0));

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView billboardTitle;
        TextView song1;
        TextView song2;
        TextView song3;
        TextView song4;
        TextView song5;
        TextView song6;
        TextView song7;
        TextView song8;
        TextView song9;
        TextView song10;


        public ViewHolder(View item){
            super(item);
            billboardTitle= item.findViewById(R.id.textView_chartTitle);
            song1=item.findViewById(R.id.textView_song1);
            song2=item.findViewById(R.id.textView_song2);
            song3=item.findViewById(R.id.textView_song3);
            song4=item.findViewById(R.id.textView_song4);
            song5=item.findViewById(R.id.textView_song5);
            song6=item.findViewById(R.id.textView_song6);
            song7=item.findViewById(R.id.textView_song7);
            song8=item.findViewById(R.id.textView_song8);
            song9=item.findViewById(R.id.textView_song9);
            song10=item.findViewById(R.id.textView_song10);

        }

        @Override
        public void onClick(View v) {
            int selected= getAdapterPosition();
            String selectedUser= billboard.get(selected);
            //here set something to make the application open that artist info
            notifyDataSetChanged();
            ((Activity)context).recreate();
        }
    }
}