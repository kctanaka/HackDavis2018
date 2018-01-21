package com.justinclass.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kimi on 1/21/2018.
 */

public class Course {
    String DCourseName;
    List<Meeting> DMeetingList;
    public Course(){
        DMeetingList = new ArrayList<Meeting>();
    }
    public void addMeeting(Meeting meeting){
        if (DMeetingList != null){
            DMeetingList.add(meeting);
        }
        else{
            //print error statement
        }
    }
    public void setCourseName(String name){
        DCourseName = name;
    }
    public String getCourseName(){
        return DCourseName;
    }

    public List<Meeting> getMeetings(){
        return DMeetingList;
    }
}
