package com.app.fishcab;

public class Time {
    private int hour, minute;

    public static String current_time;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(String currentTime) {
        Time.current_time = currentTime;
        String[] time = currentTime.split(":");
        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);
    }

    public void oneMinutePassed() {
        minute++;
        if(minute == 60) {
            hour++;
            minute = 0;
        }

        if (hour == 24) {
            hour = 0;
            System.out.println("Next day");
        }
    }

    public static void setCurrent_time(String current_time){
        Time.current_time = current_time;
    }
    public static String getCurrentTime(){
        return Time.current_time;
    }
}
