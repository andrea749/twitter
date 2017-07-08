package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

/**
 * Created by andreagarcia on 7/7/17.
 */

public class ResponseActivity extends AppCompatActivity {

    RestClient client;
    public EditText etTweetReply;
    public Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        etTweetReply = (EditText) findViewById(R.id.etTweetReply);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        etTweetReply.setText("@" + tweet.user.screenName);
    }

    public void onReply(View v) {
        client = new RestClient(this);

        // Activity finished ok, return the data
        client.sendTweet(etTweetReply.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    Intent i = new Intent(ResponseActivity.this, TimelineActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, i); // set result code and bundle data for response
                    finish(); // closes the activity, pass data to parent

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
