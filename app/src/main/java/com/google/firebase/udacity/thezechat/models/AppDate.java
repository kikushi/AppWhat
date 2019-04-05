package com.google.firebase.udacity.thezechat.models;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppDate {
    private Calendar calendate ;
    private String date ;
    private String time ;

    public AppDate() {
        calendate = Calendar.getInstance() ;
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-mm-dd") ;
        date = sdfDate.format(calendate.getTime()) ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:MM:SS") ;
        time = sdfTime.format(calendate.getTime()) ;
    }

    public String getTime(){
        Log.i("DIM", time) ;
        return time ;
    }

    public String getDate() {
        Log.i("DIM", date) ;
        return date ;
    }
}
