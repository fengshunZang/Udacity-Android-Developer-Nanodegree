package com.zangfengshun.popmovies.fetcher;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.zangfengshun.popmovies.data.Utility;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Zang on 2016-08-21.
 */
public class MovieReviewFetcher extends AsyncTaskLoader<String> {
    private final static String LOG_TAG = MovieReviewFetcher.class.getSimpleName();
    private Utility.OptionsItemType mItemType;
    private Utility.InfoType mType;
    private String mID;

    public MovieReviewFetcher(Context context, Utility.InfoType type, Utility.OptionsItemType itemType, String id) {
        super(context);
        mType = type;
        mItemType = itemType;
        mID = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        String review = null;
        try {
            URL url = Utility.getUrl(mType, mItemType, mID);
            String movieReviewDataJsonStr = Utility.getJsonStrFromUrl(url);
            review = Utility.getReviewStrFromJson(movieReviewDataJsonStr);
        } catch (MalformedURLException e) {
            Log.v(LOG_TAG, "MalformedURLException during getting review");
        } catch (JSONException e) {
            Log.v(LOG_TAG, "JSONException during getting review");
        }
        return review;
    }
}
