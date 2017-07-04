package com.codepath.apps.restclienttemplate;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by andreagarcia on 6/26/17.
 */

@Parcel
public class User {
    //list attributes and deserialize json
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String createdAt;
    public String tagLine;
    public int followersCount;
    public int followingCount;

    public static User fromJSON(JSONObject json) throws JSONException{
        User user = new User();
        //extract and fill values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");
        user.createdAt = json.getString("created_at");
        user.tagLine = json.getString("description");
        user.followersCount = json.getInt("followers_count");
        user.followingCount = json.getInt("friends_count");

        return user;
    }
}
