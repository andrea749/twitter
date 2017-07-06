package com.codepath.apps.restclienttemplate;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.SimpleDateFormat;

/**
 * Created by andreagarcia on 6/26/17.
 */

@Parcel
public class Tweet {
    //list attributes
    public String body;
    public long uid; //database ID for tweet
    public User user;
    public String createdAt;
    public SimpleDateFormat simpleDateFormat;
    public Tweet tweet;
    public String screenName;

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

}

