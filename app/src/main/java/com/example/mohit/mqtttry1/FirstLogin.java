package com.example.mohit.mqtttry1;

import android.Manifest;
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

public class FirstLogin extends AppCompatActivity {

    private String phoneNumber;
    private String Address;
    private EditText ETphoneNumber, ETAddress;
    private RadioButton locationAccess;
    private Spinner houseConfig;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean mLocationPermissionGranted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        ETphoneNumber = (EditText) findViewById(R.id.Etphone);
        ETAddress = (EditText) findViewById(R.id.EtAddress);
        locationAccess = (RadioButton) findViewById(R.id.locationRadioButton);

    }

    public void locationAccess(View view) {
        Log.d("radio button ", "pressed");
        getLocationPermission();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
                        Location currentLocation = (Location) task.getResult();
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

    }

    private Boolean checkUserInfo() {
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1 );
        }
    }


}
