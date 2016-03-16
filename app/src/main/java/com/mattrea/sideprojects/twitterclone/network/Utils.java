package com.mattrea.sideprojects.twitterclone.network;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getCurrentDateTimeFormatted(){
        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.US);
        return sf.format(new Date());
    }
}