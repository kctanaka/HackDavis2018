package com.justinclass.schedule;

import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * Created by Kimi on 1/21/2018.
 */

public class Schedule {
    List<Course> DCourseList;
    String DUniversityName;
    String DCityName;

    Geocoder DGeocoder;

    public Schedule(Geocoder geocoder){
        DCourseList = new ArrayList<Course>();
        DGeocoder = geocoder;
    }

    public Meeting nextMeeting(){
        Meeting nextMeeting = null;
        GregorianCalendar nextMeetingTime = null;
        GregorianCalendar now = new GregorianCalendar();

        if (DCourseList!= null){
            for (Course course : DCourseList){
                for (Meeting meeting : course.getMeetings()){
                    GregorianCalendar endDate = meeting.getEndDate();
                    if (now.after(endDate)){
                        continue;
                    }

                    // Might be missing checking for case where start time/date is much later than now (over 24 hrs)

                    int DayofWeek = now.get(Calendar.DAY_OF_WEEK);

                    if(meeting.DDays[DayofWeek-1]){
                        GregorianCalendar meetingEndToday = new GregorianCalendar();
                        meetingEndToday.set(Calendar.HOUR_OF_DAY, meeting.getEndTime().get(Calendar.HOUR_OF_DAY));
                        meetingEndToday.set(Calendar.MINUTE, meeting.getEndTime().get(Calendar.MINUTE));
                        if(now.after(meetingEndToday)){
                            continue;
                        }

                        if(nextMeeting == null){
                            nextMeeting = meeting;
                            nextMeetingTime = new GregorianCalendar();
                            nextMeetingTime.set(Calendar.HOUR_OF_DAY, meeting.getStartTime().get(Calendar.HOUR_OF_DAY));
                            nextMeetingTime.set(Calendar.MINUTE, meeting.getStartTime().get(Calendar.MINUTE));
                        }
                        else{
                            GregorianCalendar challengerMeetingToday = new GregorianCalendar();
                            challengerMeetingToday = new GregorianCalendar();
                            challengerMeetingToday.set(Calendar.HOUR_OF_DAY, meeting.getStartTime().get(Calendar.HOUR_OF_DAY));
                            challengerMeetingToday.set(Calendar.MINUTE, meeting.getStartTime().get(Calendar.MINUTE));
                            if (challengerMeetingToday.before(nextMeetingTime)){
                                nextMeetingTime = challengerMeetingToday;
                                nextMeeting = meeting;
                            }
                        }
                    }

                }
            }
        }
        return nextMeeting;
    }

    public void LoadSchedule(String filename){
        if (filename == null){
            // print error message?
            LoadTestData();
            return;
        }

    }
    public void LoadTestData(){
        Meeting testMeeting = new Meeting();
        testMeeting.DEndDate.set(Calendar.MONTH,Calendar.FEBRUARY);
        testMeeting.DEndDate.set(Calendar.DATE,01);
        testMeeting.DEndDate.set(Calendar.HOUR_OF_DAY,14);
        testMeeting.DEndDate.set(Calendar.MINUTE,00);


        testMeeting.DEndTime.set(Calendar.HOUR_OF_DAY,14);
        testMeeting.DEndTime.set(Calendar.MINUTE,00);


        testMeeting.DStartTime.set(Calendar.HOUR_OF_DAY,13);
        testMeeting.DStartTime.set(Calendar.MINUTE,00);

        testMeeting.setLocation("ARC Pavilion","Davis",DGeocoder);
        for(int i = 0; i<7; i++){
            testMeeting.DDays[i] = true;
        }


        Course testCourse = new Course();

        testCourse.addMeeting(testMeeting);

        DCourseList.add(testCourse);
    }
}
