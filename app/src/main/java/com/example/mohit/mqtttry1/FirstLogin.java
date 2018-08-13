package com.example.mohit.mqtttry1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstLogin extends AppCompatActivity {

    private String phoneNumber;
    private String Address ;
    private Long BHK ;
    private EditText ETphoneNumber, ETAddress;
    private RadioButton locationAccess;
    private Spinner ShouseConfig;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean mLocationPermissionGranted;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    FirebaseDatabase database;
    Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        ETphoneNumber = (EditText) findViewById(R.id.Etphone);
        ETAddress = (EditText) findViewById(R.id.EtAddress);
        locationAccess = (RadioButton) findViewById(R.id.locationRadioButton);
        ShouseConfig = (Spinner) findViewById(R.id.houseConfigSpinner);

    }

    public void locationAccess(View view) {
        Log.d("radio button ", "pressed");
        //getLocationPermission();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getBaseContext());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Log.d("location task", "successful");
                        currentLocation = (Location) task.getResult();
                        Log.d("latitude", String.valueOf(currentLocation.getLatitude()));
                        Log.d("Longitude", String.valueOf(currentLocation.getLongitude()));

                    }else {Log.d("location task","failed");}
                }
            });


    }


    public void SubmitUserInfo(View view) {
        Boolean userInfoCorrectformat = checkUserInfo();
        if(userInfoCorrectformat){
            SubmitUserInfo();
        }
        else{
            Toast.makeText(this,"Please fill in correct Details",Toast.LENGTH_LONG).show();
        }
    }

    private void SubmitUserInfo() {
        phoneNumber = ETphoneNumber.getText().toString();
        Address = ETAddress.getText().toString();
        BHK = ShouseConfig.getSelectedItemId();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        User localUser = new User(firebaseUser.getDisplayName(),firebaseUser.getEmail());
        localUser.setAddress(Address);
        localUser.setPhone(phoneNumber);
        localUser.setLatitude(currentLocation.getLatitude());
        localUser.setLongitude(currentLocation.getLongitude());
        localUser.setBhk(BHK);
        databaseReference.child("users").child(firebaseUser.getUid()).setValue(localUser);
        Intent intent = new Intent(getBaseContext(), AddDeviceInRoom.class);
        startActivity(intent);


    }

    private Boolean checkUserInfo() {
        return true;
    }






}
