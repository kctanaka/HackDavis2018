package com.justinclass;

import android.text.format.Time;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.TimerTask;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kimi on 1/21/2018.
 */

public class ClockUpdater extends TimerTask {
    TextView DTimeField;
    Activity DContext;

    public ClockUpdater(TextView timeField,AppCompatActivity activity ) {
        DTimeField = timeField;
        DContext = activity;
    }


    public void run() {
        //public void UpdateTimeDisplay (){

            Time now = new Time();

            now.setToNow();
            String rawTimeString = now.format2445();
            String rawHourString = rawTimeString.substring(9, 11); // substring(inclusive, exclusive)
            String rawMinuteString = rawTimeString.substring(11, 13);

            String formattedHourString = rawHourString;
            String AMPMString = "";

            int hourValue = Integer.parseInt(rawHourString);
            if (hourValue < 12) {
                AMPMString = "AM";
                if (hourValue == 0) {
                    formattedHourString = "12";
                }
                // otherwise it stays as set
            } else {
                AMPMString = "PM";
                hourValue = hourValue - 12;
                if (hourValue == 0) {
                    formattedHourString = "12";
                } else {
                    formattedHourString = String.valueOf(hourValue);
                }
            }

        final String formattedTimeString = formattedHourString + ":" + rawMinuteString + " " + AMPMString;
        DContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                DTimeField.setText(formattedTimeString);
            }
        });


        }

    }