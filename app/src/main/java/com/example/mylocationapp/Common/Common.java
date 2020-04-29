package com.example.mylocationapp.Common;

import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID="63b9444e23d3ad95bb3984ff24dc8948";
    public static Location current_location ;


    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm EEE dd MM yyyy");
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToHour(long sunrise) {
        Date date = new Date(sunrise*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);
        return formatted;

    }
}
