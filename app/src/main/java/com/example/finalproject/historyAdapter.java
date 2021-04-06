package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.ViewHolder> {

    private List<History> histories;
    private DatabaseHelper databaseHelper;
    private Context context;

    public historyAdapter(List<History> histories, DatabaseHelper databaseHelper, Context context){
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
        History history= histories.get(position);
        holder.time.setText(history.getDate());
        holder.searched.setText(history.getSearchText());
        holder.date.setText(history.getDate());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView searched;
        TextView date;
        TextView time;
        Button delete;
        public ViewHolder(View item){
            super(item);
            searched=item.findViewById(R.id.textView_searched);
            time= item.findViewById(R.id.textView_time);
            date=item.findViewById(R.id.textView_date);
            delete= item.findViewById(R.id.button_delete);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //when button delete is clicked, I want to call the delete user method
            //on this user
            int selected= getAdapterPosition();
            History selectedH= histories.get(selected);
            databaseHelper.deleteUser(selectedH);
            Toast.makeText(context, selectedH.toString(), Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
    }

}
