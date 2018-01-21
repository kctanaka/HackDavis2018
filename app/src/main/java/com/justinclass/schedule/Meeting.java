package com.justinclass.schedule;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.format.Time;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by Kimi on 1/21/2018.
 */

public class Meeting {
    String DType = "Class"; // mostly optional to set. E.g. lecture, lab, discussion, etc.
    String DLocation;
    Address DAddress;
    boolean[] DDays = new boolean[]{false,false,false,false,false,false,false};
    Time DStartTime;
    Time DEndTime;
    Time DEndDate;

    public Meeting(){
    }

    public void setStartTime(Time startTime){
        DStartTime = startTime;
    }
    public Time getStartTime(){
        return DStartTime;
    }

    public void setEndTime(Time endTime){
        DEndTime = endTime;
    }
    public Time getEndTime(){
        return DEndTime;
    }

    public void setEndDate(Time endDate){
        DEndDate = endDate;
    }
    public Time getEndDate(){
        return DEndDate;
    }

    public void setLocation(String location, String city, Geocoder geocoder){
        DLocation = location;

        try{
            DAddress = geocoder.getFromLocationName(location + "," + city,1).get(0);
        }
        catch (IOException ex){
            DAddress = null;
        }
    }
    public Address getAddress(){
        return DAddress;
    }


}
