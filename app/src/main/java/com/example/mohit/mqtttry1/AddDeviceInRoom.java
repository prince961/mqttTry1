package com.example.mohit.mqtttry1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class AddDeviceInRoom extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RoomListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Room> roomArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_in_room);

        mRecyclerView = (RecyclerView) findViewById(R.id.RVRoomList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Room masterbedroom = new Room("Master Bedroom","Bedroom");
        Room bathroomAttached = new Room("Bathroom","Bathroom");
        Room kitchen = new Room("Kitchen", "Kitchen");
        Room livingRoom  = new Room("LivingRoom", "LivingRoom");
        Room lobby = new Room("Lobby","Lobby");

        roomArrayList = new ArrayList<Room>();
        roomArrayList.add(masterbedroom);
        roomArrayList.add(bathroomAttached);
        roomArrayList.add(kitchen);
        roomArrayList.add(livingRoom);



        mAdapter = new RoomListAdapter(roomArrayList,this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
