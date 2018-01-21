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
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

//import com.justinclass.schedule.Meeting;
/**
 * Created by Kimi on 1/21/2018.
 */

public class Schedule {
    List<Course> DCourseList;
    List<Meeting> DMeetingList;
    String DUniversityName;
    String DCityName;

    public Schedule(){
        DCourseList = new ArrayList<Course>();
    }

    public void LoadSchedule(){
        try {
            String path = System.getProperty("user.dir");
            path = path.split("src")[0] + "sampledata";
            File file = new File(path);
            BufferedReader b = new BufferedReader(new FileReader(file));
            Boolean first = Boolean.TRUE;
            DMeetingList = new ArrayList<Meeting>();
            Meeting meeting = new Meeting();
            for(String line; (line = b.readLine()) != null; ) {
                line = line.trim();
                System.out.println(line);

                if(line.contains("BEGIN:VEVENT")){
                    if(!first){
                        DMeetingList.add(meeting);
                    }
                    first = Boolean.FALSE;
                    meeting = new Meeting();
                }
                else if(line.contains("DTSTART")){
                    if(line.contains("DTSTART;")){
                        line = line.split("=")[1];
                    }
                    line = line.split(":")[1];
                    String date = line.split("T")[0];
                    String time = line.split("T")[1];
                    meeting.DStartTime.set(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(4,6)),Integer.parseInt(date.substring(6,8)), Integer.parseInt(time.substring(0,2)), Integer.parseInt(time.substring(2,4)),Integer.parseInt(time.substring(4,6)));

                }
                else if(line.contains("DTEND")){
                    if(line.contains("DTEND;")){
                        line = line.split("=")[1];
                    }
                    line = line.split(":")[1];
                    String date = line.split("T")[0];
                    String time = line.split("T")[1];
                    meeting.DEndTime.set(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(4,6)),Integer.parseInt(date.substring(6,8)), Integer.parseInt(time.substring(0,2)), Integer.parseInt(time.substring(2,4)),Integer.parseInt(time.substring(4,6)));

                }
                else if(line.contains("LOCATION:")){
                    meeting.DLocation = line.split(":")[1];
                }
                else if(line.contains("SUMMARY:")){
                    meeting.DCourse = line.split(":")[1];
                }
                else if(line.contains("RRULE:")){
                    line = line.split(":")[1];
                    meeting.DFrequency = Integer.parseInt(line.split(";")[0].split("=")[1]);
                    meeting.DCount = Integer.parseInt(line.split(";")[1].split("=")[1]);
                    for(String day : line.split(";")[2].split("=")[1].split(",")){
                        if(day == "MO"){
                            meeting.DDays[Calendar.MONDAY-1] = Boolean.TRUE;
                        }else if(day == "TU"){
                            meeting.DDays[Calendar.TUESDAY-1] = Boolean.TRUE;

                        }else if(day == "WE"){
                            meeting.DDays[Calendar.WEDNESDAY-1] = Boolean.TRUE;

                        }else if(day == "TH"){
                            meeting.DDays[Calendar.THURSDAY-1] = Boolean.TRUE;

                        }else if(day == "FR"){
                            meeting.DDays[Calendar.TUESDAY-1] = Boolean.TRUE;

                        }else if(day == "SA"){
                            meeting.DDays[Calendar.SATURDAY-1] = Boolean.TRUE;

                        }else if(day == "SU"){
                            meeting.DDays[Calendar.SUNDAY-1] = Boolean.TRUE;

                        }
                    }
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
