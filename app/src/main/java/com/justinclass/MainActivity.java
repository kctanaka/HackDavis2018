package com.justinclass;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.provider.Settings;
import android.widget.TextView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.*;

// Permission stuff
import android.support.v4.content.ContextCompat;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.Manifest;

import android.support.v7.app.AlertDialog;

import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import android.text.format.Time;


public class MainActivity extends AppCompatActivity {

    /* Location variables */
    public static final int MY_REQUEST_LOCATION = 4;
    LocationManager locationManager;

    Geocoder geocoder;

    double longitudeBest, latitudeBest;
    TextView longitudeValueBest, latitudeValueBest;
    TextView placeName;

    //double classLongitude, classLatitude;
    TextView classLongitudeValue, classLatitudeValue;
    TextView classLocationName;

    String classLocationInput = "Wellman Hall, Davis";

    /* Time variables */
    TextView timeField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Time code */
        timeField = (TextView) findViewById(R.id.currentTime);
        Timer timer = new Timer();
        int period = 5000;//5secs
        timer.schedule(new ClockUpdater(timeField, this), new Date(), period );

        /* Location code */

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.US);

        longitudeValueBest = (TextView) findViewById(R.id.longitudeValueBest);
        latitudeValueBest = (TextView) findViewById(R.id.latitudeValueBest);
        placeName = (TextView) findViewById(R.id.locationName);

        classLongitudeValue = (TextView) findViewById(R.id.classLongitude);
        classLatitudeValue = (TextView) findViewById(R.id.classLatitude);
        classLocationName = (TextView) findViewById(R.id.classLocationName);

        classLocationName.setText(classLocationInput);

        try{
            Address classAddress = geocoder.getFromLocationName(classLocationInput,1).get(0);
            if (classAddress != null){
                classLongitudeValue.setText(String.valueOf(classAddress.getLongitude()));
                classLatitudeValue.setText(String.valueOf(classAddress.getLatitude()));
            }

        }
        catch (IOException ex){
            classLongitudeValue.setText("N/A");
            classLatitudeValue.setText("N/A");
        }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                }
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 2 * 60 * 1000, 10, locationListenerBest);
            Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
        }


        /*String provider = locationManager.getBestProvider(criteria, true);
        while (provider == null) {
            provider = locationManager.getBestProvider(criteria, true);
            if (provider != null) {
                locationManager.requestLocationUpdates(provider, 2 * 60 * 1000, 10, locationListenerBest);
                Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
            }

        while (provider != LocationManager.GPS_PROVIDER){
            provider = locationManager.getBestProvider(criteria, true);
            if (provider == LocationManager.GPS_PROVIDER) {
                locationManager.requestLocationUpdates(provider, 2 * 60 * 1000, 10, locationListenerBest);
                Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
            }
        }*/

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
//        if (!checkLocation()){
//            showAlert();
//
//        }


    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    longitudeValueBest.setText(longitudeBest + "");
                    latitudeValueBest.setText(latitudeBest + "");
                    Toast.makeText(MainActivity.this, "Best Provider update", Toast.LENGTH_SHORT).show();
                    try{
                        Address address = geocoder.getFromLocation(latitudeBest,longitudeBest,1).get(0);
                        if (address.getFeatureName() != null){
                            placeName.setText(address.getFeatureName());
                        }
                        else
                        {
                            placeName.setText("N/A");
                        }
                    }
                    catch (IOException ex){
                        placeName.setText("N/A");
                    }

                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}
