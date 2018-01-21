package com.justinclass;



import com.justinclass.schedule.*;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;

import android.text.format.Time;


public class MainActivity extends AppCompatActivity {
    Schedule schedule;

    /* Location variables */
    public static final int MY_REQUEST_LOCATION = 4;

    public static final double ACCURACY_VALUE = 0.0002;


    LocationManager locationManager;

    Geocoder geocoder;

    double longitudeBest, latitudeBest;
    TextView longitudeValueBest, latitudeValueBest;
    TextView line1Value,line2Value;
    TextView placeName;

    //double classLongitude, classLatitude;
    TextView classLongitudeValue, classLatitudeValue;
    TextView classLocationName;

    Button button;

    String classLocationInput = "Wellman Hall, Davis";


    boolean DAtLocation;
    Meeting DNextMeeting = null;

    /* Time variables */
    TextView timeField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        String filename;

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            filename = null;
        } else {
            filename = extras.getString("EXTRA_MESSAGE");
        }

        geocoder = new Geocoder(this, Locale.US);
        schedule = new Schedule(geocoder);
        //schedule.LoadSchedule(filename);
        schedule.LoadSchedule(null);

        /* Time code */
        timeField = (TextView) findViewById(R.id.currentTime);
        Timer timer = new Timer();
        int period = 5000;//5secs
        timer.schedule(new ClockUpdater(timeField, this), new Date(), period );

        /* Location code */

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        longitudeValueBest = (TextView) findViewById(R.id.longitudeValueBest);
        latitudeValueBest = (TextView) findViewById(R.id.latitudeValueBest);
        placeName = (TextView) findViewById(R.id.locationName);

        classLongitudeValue = (TextView) findViewById(R.id.classLongitude);
        classLatitudeValue = (TextView) findViewById(R.id.classLatitude);
        classLocationName = (TextView) findViewById(R.id.classLocationName);

        button = (Button) findViewById(R.id.centerActionButton);

        classLocationName.setText(classLocationInput);


        line1Value = (TextView) findViewById(R.id.line1);
        line2Value = (TextView) findViewById(R.id.line2);

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

    private boolean IsAtNextMeeting(Meeting nextMeeting){
        //Meeting nextMeeting = schedule.nextMeeting(); // do once to reduce overhead.
        if (nextMeeting == null){
            return false; // deal with somewhere else
        }
        else{
            if(Math.abs(longitudeBest - nextMeeting.getAddress().getLongitude()) < ACCURACY_VALUE &&
                    Math.abs(latitudeBest - nextMeeting.getAddress().getLatitude()) < ACCURACY_VALUE){
                return true;
            }
            else {
                return false;
            }
        }
    }

    private int MinutesEarly(GregorianCalendar meetingTime){
        int minutesEarly, hoursEarly;
        GregorianCalendar now = new GregorianCalendar();
        minutesEarly = meetingTime.get(Calendar.MINUTE) - now.get(Calendar.MINUTE);
        hoursEarly = meetingTime.get(Calendar.HOUR_OF_DAY) - now.get(Calendar.HOUR_OF_DAY);
        minutesEarly = hoursEarly * 60 + minutesEarly;
        return minutesEarly;
    }

    public void buttonCallBack(View view) {
        if(DNextMeeting == null){
            return;
        }
        if(DAtLocation) {
            double meetingLat, meetingLong;
            meetingLat = DNextMeeting.getAddress().getLatitude();
            meetingLong = DNextMeeting.getAddress().getLongitude();
            Uri gmmIntentUri = Uri.parse("google.navigation:geo:" + String.valueOf(meetingLat) + "," + String.valueOf(meetingLong));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            Intent lockintent = new Intent(this, PhoneLockActivity.class);
            startActivity(lockintent);
        }
    }


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

                        DNextMeeting = schedule.nextMeeting();
                        if (DNextMeeting !=null) {
                            DAtLocation = IsAtNextMeeting(DNextMeeting);
                            int minsTillClass = MinutesEarly(DNextMeeting.getStartTime());
                            if (DAtLocation) {
                                if (minsTillClass > 0) {
                                    line1Value.setText("You arrived " + String.valueOf(minsTillClass) + " minutes early!");
                                    line2Value.setText("Great job!");
                                    button.setText("@string/checkin_button");
                                }
                                if (minsTillClass == 0) {
                                    line1Value.setText("You arrived on time!");
                                    line2Value.setText("Good job!");
                                    button.setText("@string/checkin_button");
                                }
                                if (minsTillClass < 0) {
                                    line1Value.setText("You arrived " + String.valueOf(Math.abs(minsTillClass)) + " minutes late.");
                                    line2Value.setText("We can do better next time!");
                                    button.setText("@string/checkin_button");
                                }

                            } else {
                                line1Value.setText("Next class in " + String.valueOf(minsTillClass));
                                line2Value.setText("At " + DNextMeeting.getLocationName());
                                button.setText("@string/go_button");
                            }
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
