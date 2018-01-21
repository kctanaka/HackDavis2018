package com.justinclass.schedule;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.format.Time;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Kimi on 1/21/2018.
 */

public class Meeting {
    String DType = "Class"; // mostly optional to set. E.g. lecture, lab, discussion, etc.
    String DLocation;
    Address DAddress;
    boolean[] DDays = new boolean[]{false,false,false,false,false,false,false};
    int NumMeetings;
    GregorianCalendar DStartTime;
    GregorianCalendar DEndTime;
    GregorianCalendar DEndDate;

    public Meeting(){
    }

    public void setStartTime(GregorianCalendar startTime){
        DStartTime = startTime;
    }
    public GregorianCalendar getStartTime(){
        return DStartTime;
    }

    public void setEndTime(GregorianCalendar endTime){
        DEndTime = endTime;
    }
    public GregorianCalendar getEndTime(){
        return DEndTime;
    }

    public void setEndDate(GregorianCalendar endDate){
        DEndDate = endDate;
    }
    public GregorianCalendar getEndDate(){
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
