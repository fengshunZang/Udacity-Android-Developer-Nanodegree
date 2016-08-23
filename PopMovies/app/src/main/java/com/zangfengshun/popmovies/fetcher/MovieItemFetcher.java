package com.zangfengshun.popmovies.fetcher;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.zangfengshun.popmovies.data.MovieDbHelper;
import com.zangfengshun.popmovies.data.Utility;
import com.zangfengshun.popmovies.item.MovieItem;
import org.json.JSONException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Zang on 2016-08-21.
 */
public class MovieItemFetcher extends AsyncTaskLoader<ArrayList<MovieItem>> {
    private static final String LOG_TAG = MovieItemFetcher.class.getSimpleName();
    private Utility.OptionsItemType mItemType;
    private Utility.InfoType mType;
    private MovieDbHelper mDbHelper;
    private Context mContext;

    //Constructor
    public MovieItemFetcher(Context context, Utility.InfoType type, Utility.OptionsItemType itemType) {
        super(context);
        mContext = context;
        mItemType = itemType;
        mType = type;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<MovieItem> loadInBackground() {
        ArrayList<MovieItem> movieItems = new ArrayList<>();

        switch (mItemType) {
            case FAVORITE:
                mDbHelper = new MovieDbHelper(mContext);
                movieItems = mDbHelper.queryFavoriteMovieEntries();
                return movieItems;
            default:
                try {
                    URL url = Utility.getUrl(mType, mItemType, "");
                    //Will contain the raw JSON response as a string.
                    String movieDataJsonStr = Utility.getJsonStrFromUrl(url);

                    if (mItemType != Utility.OptionsItemType.FAVORITE) {
                        movieItems = Utility.getMovieDataFromJson(movieDataJsonStr);
                    }
                } catch (MalformedURLException e) {
                    Log.v(LOG_TAG, "MalformedURLException");
                } catch (JSONException e) {
                    Log.v(LOG_TAG, "JSONException during json data parsing");
                }
                break;
        }
        return movieItems;
    }
}
