package com.codepath.apps.restclienttemplate;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by andreagarcia on 6/26/17.
 */

public class Tweet {
    //list attributes
    public String body;
    public long uid; //database ID for tweet
    public User user;
    public String createdAt;
    public SimpleDateFormat simpleDateFormat;
    public Tweet tweet;

    //deserialize JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        //extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }



//    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
//    public String getRelativeTimeAgo(String rawJsonDate) {
//        public time = rawJsonDate
//        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
//        simpleDateFormat = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
//        simpleDateFormat.setLenient(true);
//
//        String relativeDate = "";
//        try {
//            long dateMillis = simpleDateFormat.time.getTime();
//            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return relativeDate;
    }
}
