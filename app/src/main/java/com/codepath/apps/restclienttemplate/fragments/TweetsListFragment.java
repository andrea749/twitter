package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.Tweet;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by andreagarcia on 7/3/17.
 */

public class TweetsListFragment extends Fragment implements  TweetAdapter.TweetAdapterListener {

    public interface TweetSelectedListener {
        //handle tweet selection
        public void onTweetSelected(Tweet tweet);
    }
    private SwipeRefreshLayout swipeContainer;
    private EndlessScrollListener scrollListener;
    ArrayList<Tweet> tweets = new ArrayList<>();
    TweetAdapter tweetAdapter;
    RecyclerView rvTweets;
    private RestClient client;

    //inflation happens inside onCreateView

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate layout
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);

        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        tweetAdapter = new TweetAdapter(tweets, this);
        //recyclerview setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        //set adapter
        rvTweets.setAdapter(tweetAdapter);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
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
        return v;

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

    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

//        Log.i(TAG, client.toString());
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

    public void addItems(JSONArray response) {
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
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        //getContext() because inside fragment
        //fragment can call back on activity through getActivity()
        ((TweetSelectedListener) getActivity()).onTweetSelected(tweet);
    }
}
