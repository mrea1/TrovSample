package com.mattrea.sideprojects.twitterclone.model;

import android.content.Context;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by h141764 on 3/6/16.
 */

public class Tweet extends SugarRecord {

    @Unique
    public String tweet_id;

    public String body;

    public String timestamp;

    public String user;

    public String user_image_url;

    //returns time posted -- example: "10m", "1h", "1d", "1w", "1y"
    public String getTimePosted(){
        Date date = null;
        try {
            date = getTwitterDate(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date now = new Date();

        if (date!=null)
        {
            long duration  = now.getTime() - date.getTime();

            long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
            int diffInDays = (int) TimeUnit.MILLISECONDS.toDays(duration);

            if (diffInDays>=1 && diffInDays<7)
                return String.valueOf(diffInDays)+"d";
            else if(diffInDays>=7 && diffInDays<365)
                return String.valueOf(diffInDays/7)+"w";
            else if(diffInDays>=365)
                return String.valueOf(diffInDays/365)+"y";
            else if(diffInDays<1 && diffInHours>=1)
                return String.valueOf(diffInHours)+"h";
            else if(diffInHours<1 && diffInMinutes>=1)
                return String.valueOf(diffInMinutes)+"m";
            else if(diffInMinutes<1 && diffInSeconds>=1)
                return String.valueOf(diffInSeconds)+"s";
            else
                return "Now";
        }else
            return "?";
    }

    public static Date getTwitterDate(String date) throws ParseException {

        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.US);
        sf.setLenient(true);
        return sf.parse(date);
    }

    @Override
    public String toString() {
        return "ID "+tweet_id+"\nDate "+timestamp+"\nBody "+body;
    }


}
