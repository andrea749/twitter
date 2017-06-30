package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();
    private RestClient client;
    private SwipeRefreshLayout swipeContainer;
    ArrayList<Tweet> tweets = new ArrayList<>();
    TweetAdapter tweetAdapter = new TweetAdapter(tweets);
    RecyclerView rvTweets;
    private EndlessScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        //recyclerview setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        //set adapter
        rvTweets.setAdapter(tweetAdapter);
        client = RestApplication.getRestClient();
        populateTimeline();

        //retain instance so that you can call 'resetState()' for fresh searches
        scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //triggered only when new data needs to be appended to list
                //List<Tweet> moreTweets = new ArrayList<>();
                //int curSize = tweetAdapter.getItemCount();
                //add whatever code is needed to append new items to bottom of list -- TODO
                Long maxId = tweets.get(tweets.size() -1).uid;
                loadNextDataFromApi(maxId);
            }
        };


        //adds scroll listener to Recyclerview
        rvTweets.addOnScrollListener(scrollListener);


//        client = RestApplication.getRestClient();
//        populateTimeline();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void loadNextDataFromApi(Long maxId) {
        //send API request to retrieve paginated data
        client.getNextTweets(maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        // send request including offset value (page) as query parameter
        //deserialize(/deparcel?) and construct new model objects from api response
        //append new data objects to existing set inside array
        //notify adapter of new items with notifyItemRangeInserted()
    }

        public void fetchTimelineAsync(int page) {
            // Send the network request to fetch the updated data
            // `client` here is an instance of Android Async HTTP
            // getHomeTimeline is an example endpoint.

            Log.i(TAG, client.toString());
            client.getHomeTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // Remember to CLEAR OUT old items before appending in the new ones
                    tweetAdapter.clear();
                    // s1 clear
                    tweets.clear();
                    //  s2 loop thru all json objects in the array, convert each json obj into a Tweet object, put each tweet obj into tweets
                    for (int i = 0; i < response.length(); i++){
                        try {
                            Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                            tweets.add(tweet);
                            tweetAdapter.notifyItemInserted(tweets.size() - 1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // ...the data has come back, add new items to your adapter...
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("DEBUG", "Fetch timeline error: " + responseString);
                    swipeContainer.setRefreshing(false);

                }
            });

    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }


    private final int REQUEST_CODE = 20;
    // FirstActivity, launching an activity for a result
    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        i.putExtra("mode", 2); // pass arbitrary data to launched activity
        startActivityForResult(i, REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            //String tweet = data.getExtras().getString("tweet");
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            // Toast the name to display temporarily on screen
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
            //client.sendTweet(tweet);
        }

    }


    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("RestClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Log.d("RestClient", response.toString());
                //iterate through JSON array
                //for each entry deserialize JSON object

                for (int i = 0; i < response.length(); i++) {
                    //convert each object to Tweet model
                    //add tweet model to data source
                    //notify adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("RestClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("RestClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("RestClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
