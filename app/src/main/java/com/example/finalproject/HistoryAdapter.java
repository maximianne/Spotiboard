package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<History> histories;
    private DatabaseHelper databaseHelper;
    private Context context;

    public HistoryAdapter(List<History> histories, DatabaseHelper databaseHelper,
                          Context context){
        this.histories=histories;
        this.context=context;
        this.databaseHelper = databaseHelper;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);

        View userView = inflater.inflate(R.layout.item_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(userView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History user= histories.get(position);
        holder.name.setText(user.getSearched());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        ImageView delete;
        public ViewHolder(View item){
            super(item);
            name=item.findViewById(R.id.textView5);
            delete=item.findViewById(R.id.imageView3);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int selected= getAdapterPosition();
            History selectedUser= histories.get(selected);
            databaseHelper.deleteHistory(selectedUser);
            notifyDataSetChanged();
            ((Activity)context).recreate();
        }
    }
}