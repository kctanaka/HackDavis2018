package com.justinclass.schedule;

import android.support.annotation.NonNull;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Kimi on 1/21/2018.
 */

public class Schedule {
    List<Course> DCourseList;
    String DUniversityName;
    String DCityName;

    public Schedule(){
        DCourseList = new ArrayList<Course>();
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

                    if(meeting.DDays[DayofWeek]){
                        GregorianCalendar meetingEndToday = new GregorianCalendar();
                        meetingEndToday.set(Calendar.HOUR, meeting.getEndTime().get(Calendar.HOUR));
                        meetingEndToday.set(Calendar.MINUTE, meeting.getEndTime().get(Calendar.MINUTE));
                        if(meetingEndToday.after(now)){
                            continue;
                        }

                        if(nextMeeting == null){
                            nextMeeting = meeting;
                            nextMeetingTime = new GregorianCalendar();
                            nextMeetingTime.set(Calendar.HOUR, meeting.getStartTime().get(Calendar.HOUR));
                            nextMeetingTime.set(Calendar.MINUTE, meeting.getStartTime().get(Calendar.MINUTE));
                        }
                        else{
                            GregorianCalendar challengerMeetingToday = new GregorianCalendar();
                            challengerMeetingToday = new GregorianCalendar();
                            challengerMeetingToday.set(Calendar.HOUR, meeting.getStartTime().get(Calendar.HOUR));
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

    public void LoadSchedule(){

    }
}
