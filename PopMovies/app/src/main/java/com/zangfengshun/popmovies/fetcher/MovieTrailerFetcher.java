package com.zangfengshun.popmovies.fetcher;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.zangfengshun.popmovies.data.Utility;
import com.zangfengshun.popmovies.item.TrailerItem;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Zang on 2016-08-21.
 */
public class MovieTrailerFetcher extends AsyncTaskLoader<ArrayList<TrailerItem>> {
    private final static String LOG_TAG = MovieTrailerFetcher.class.getSimpleName();
    private Utility.OptionsItemType mItemType;
    private Utility.InfoType mType;
    private String mID;

    public MovieTrailerFetcher(Context context, Utility.InfoType type, Utility.OptionsItemType itemType, String id) {
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
    public ArrayList<TrailerItem> loadInBackground() {
        ArrayList<TrailerItem> trailerItems = new ArrayList<>();
        try {
            URL url = Utility.getUrl(mType, mItemType, mID);
            String movieTrailerDataJsonStr = Utility.getJsonStrFromUrl(url);
            trailerItems = Utility.getTrailerInfoFromJson(movieTrailerDataJsonStr);
        } catch (MalformedURLException e) {
            Log.v(LOG_TAG, "MalformedURLException");
        } catch (JSONException e) {
            Log.v(LOG_TAG, "JSONException");
        }
        return trailerItems;
    }
}
