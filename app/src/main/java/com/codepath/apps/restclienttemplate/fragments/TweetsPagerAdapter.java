package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreagarcia on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;
    public List<TweetsListFragment> tweetsList = new ArrayList<>();

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //return total # of fragments -- like in other adapter, return get count method

    @Override
    public int getCount() {
        return 2;
    }

    //return the fragment to use depending on position

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            HomeTimelineFragment homeTimeline = new HomeTimelineFragment();
            tweetsList.add(position, homeTimeline);
            return homeTimeline;
        } else if (position == 1) {
            MentionsTimelineFragment mentionsTimeline = new MentionsTimelineFragment();
            tweetsList.add(position, mentionsTimeline);
            return mentionsTimeline;
        } else {
            return null;
        }
    }

    //return fragment title

    @Override
    public CharSequence getPageTitle(int position) {
        //generate title based on item position
        return tabTitles[position];
    }
}
