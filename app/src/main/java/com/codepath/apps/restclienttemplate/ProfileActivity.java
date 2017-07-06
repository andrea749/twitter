package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {

    RestClient client;
    String screenName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final User otherUser = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        if (otherUser != null) {
            screenName = otherUser.screenName;
        } else {
            screenName = getIntent().getStringExtra("screen_name");
        }
        //create user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
        //display user timeline fragment inside container (dynamically because we are passing in a screen name)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //make change
        ft.replace(R.id.flContainer, userTimelineFragment);
        //commit it
        ft.commit();

        if (otherUser != null) {
            client = RestApplication.getRestClient();
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //deserialize user object
                    //User user = User.fromJSON(response);
                    //set title of actionbar based on user info
                    getSupportActionBar().setTitle(otherUser.screenName);
                    //populate user headline
                    populateUserHeadline(otherUser);
                }
            });
        } else {
            client = RestApplication.getRestClient();
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //deserialize user object
                    try {
                        User user = User.fromJSON(response);
                        //set title of actionbar based on user info
                        getSupportActionBar().setTitle(user.screenName);
                        //populate user headline
                        populateUserHeadline(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void populateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);

        tvTagline.setText(user.tagLine);
        tvFollowers.setText(user.followersCount + " Followers");
        tvFollowing.setText(user.followingCount + " Following");
        //load profile image with glide
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);



    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        //implement code for selecting tweet on profile -- lead back to profile probably
    }
}
