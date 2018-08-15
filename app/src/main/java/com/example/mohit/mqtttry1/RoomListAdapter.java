package com.example.mohit.mqtttry1;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RoomListAdapter extends RecyclerView.Adapter <RoomListAdapter.ViewHolder> {

    private Room[] roomList ;
    private Context context;

    public  RoomListAdapter (Room[] CroomList, Context context){
        this.roomList = CroomList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView heading ;
        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            heading = (TextView) v.findViewById(R.id.RoomNameTV);
        }
    }

    @NonNull
    @Override
    public RoomListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
