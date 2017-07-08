package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    RestClient client;
    public Tweet tweet;
    public ImageView ivProfile;
    public TextView tvName;
    public TextView tvHandle;
    public TextView tvBody;
    public TextView tvTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfile);
        tvName.setText(tweet.user.name);
        tvHandle.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvTimeStamp.setText(tweet.createdAt);

    }
}
