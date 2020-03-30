package controller;

import java.util.Calendar;
import java.util.Date;

public class Day {

    private int day;
    private int hour;
    private int minute;

    public Day(){
        this.day = 1;
        this.hour = 14;
        this.minute = 0;
    }

    Date getDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hour + 24*day);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    public void nextHour() {
        if (hour < 24) {
            this.hour++;
        } else {
           this.hour = 0;
        }
    }

    public void nextMinute() {
       this.minute ++;
    }

    public int getMinute(){
        return this.minute;
    }

    public void resetMinute(){
        this.minute = 0;
    }

    public void resetHour(){
        this.hour = 0;
    }

    public int getHour() {
        return this.hour;
    }

    private String getStringMinute(){
        if(minute < 10){
            return "0" + minute;
        } else {
            return Integer.toString(minute);
        }
    }

    public String getTimeOfDay() {
        if(hour == 0){
            return "Midnight";
        } else if (hour == 12){
            return "Noon";
        } else if (hour > 11){
            return hour-12 + ":" + getStringMinute()+ "pm";
        } else {
            return hour + ":" + getStringMinute()+ "am";
        }
    }

    public void nextDay(){
        this.day ++;
    }

    public int getDay(){
        return this.day;
    }

}
