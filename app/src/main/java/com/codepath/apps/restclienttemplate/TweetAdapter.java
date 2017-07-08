package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by andreagarcia on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private List<Tweet> mTweets;
    Context context;
    private TweetAdapterListener mListener;
    Tweet tweet;
    //define an interface required by viewholder



    public interface TweetAdapterListener {
        public void onItemSelected(View view, int position);
    }

    //pass in Tweets array in constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;

    }
    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
    //for each row, inflate layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    //bind values based on position of element


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get data according to position
        tweet = mTweets.get(position);

        //populate views according to data -- currently username and body, later use glide for rest
        holder.tvUsername.setText(tweet.user.name);
        holder.tvHandle.setText("@" + tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.createdAt));
        holder.tvRetweet.setText(Integer.toString(tweet.retweetCount));
        holder.tvLike.setText(Integer.toString(tweet.favoriteCount));
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public ImageButton ibComment;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvHandle;
        public TextView tvTimeStamp;
        public TextView tvLike;
        public TextView tvRetweet;


        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            ibComment = (ImageButton) itemView.findViewById(R.id.ibComment);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvRetweet = (TextView) itemView.findViewById(R.id.tvRetweet);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);

            tvBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, TweetDetailActivity.class);
                    i.putExtra("tweet", Parcels.wrap(mTweets.get(position)));
                    context.startActivity(i);
                }
            });

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("user", Parcels.wrap(mTweets.get(position).user));
                    context.startActivity(i);
                }
            });

            //handle row click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null);
                    //get position of row element
                    int position = getAdapterPosition();
                    //fire listener callback only if listener isnt null
                    mListener.onItemSelected(view, position);
                }
            });

            // FirstActivity, launching an activity for a result
            final int REQUEST_CODE = 20;
            ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(context, ResponseActivity.class);
                    i.putExtra("tweet", Parcels.wrap(mTweets.get(position)));
                    ((Activity) context).startActivityForResult(i, REQUEST_CODE);
                }
            });

        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
