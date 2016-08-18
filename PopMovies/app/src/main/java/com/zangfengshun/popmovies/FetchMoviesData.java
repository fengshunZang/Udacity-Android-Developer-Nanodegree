package com.zangfengshun.popmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zangfengshun.popmovies.adapter.MovieInfoAdapter;
import com.zangfengshun.popmovies.adapter.MovieTrailerAdapter;
import com.zangfengshun.popmovies.data.MovieDbHelper;
import com.zangfengshun.popmovies.item.MovieItem;
import com.zangfengshun.popmovies.item.TrailerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Zang on 2016-08-02.
 */
class FetchMoviesData extends AsyncTask<String, String, String> {
    private final String LOG_TAG = FetchMoviesData.class.getSimpleName();
    private ArrayList<MovieItem> mMovieInfo;
    private MovieInfoAdapter mImageAdapter;
    private MovieDbHelper mDbHelper;

    Activity mActivity;
    private Context mContext;
    public enum InfoType {GENERAL, TRAILER, REVIEW}
    public enum OptionsItemType {POPULAR, TOP_RATE, FAVORITE}
    private OptionsItemType mItemType = OptionsItemType.POPULAR;
    private InfoType mType;
    private String mID = null;

    //Constructor
    public FetchMoviesData(Activity activity, Context context, OptionsItemType itemType, InfoType type) {
        mActivity = activity;
        mContext = context;
        mItemType = itemType;
        mType = type;
    }

    public FetchMoviesData(Activity activity, Context context, InfoType type, String id) {
        mActivity = activity;
        mContext = context;
        mType = type;
        mID = id;
    }

    //This method is used to fetch raw json string data from Internet.
    @Override
    protected String doInBackground(String... params) {
        String mMovieDataJsonStr = null; //maybe set to ""
        switch (mItemType) {
            case FAVORITE:
                mDbHelper = new MovieDbHelper(mContext);
                mMovieInfo = mDbHelper.queryFavoriteMovieEntries();
                break;
            default:
                try {
                    URL url = getUrl(mType, mID);
                    //Will contain the raw JSON response as a string.
                    mMovieDataJsonStr = getJsonStrFromUrl(url);
                } catch (MalformedURLException e) {
                    Log.v(LOG_TAG, "MalformedURLException");
                }
                break;
        }
        return mMovieDataJsonStr;
    }

    //After getting data, this method shows images on main screen and handles item click event.
    @Override
    protected void onPostExecute(String jsonStr) {
        super.onPostExecute(jsonStr);

        switch (mType) {
            case GENERAL:
                ArrayList<MovieItem> _movieImagePostPath = new ArrayList<>();

                if (mItemType != OptionsItemType.FAVORITE) {
                    try {
                        _movieImagePostPath = getMovieDataFromJson(jsonStr);
                    } catch (JSONException e) {
                        Log.v(LOG_TAG, "JSONException during json data parsing");
                    }
                    mMovieInfo = _movieImagePostPath;
                }
                GridView gridView = (GridView) mActivity.findViewById(R.id.main_activity_grid_view);
                mImageAdapter = new MovieInfoAdapter(mContext, mMovieInfo);
                gridView.setAdapter(mImageAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        MovieItem item = mImageAdapter.getItem(position);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("par_key", item);
                        mActivity.startActivity(intent);
                    }
                });
                break;
            case TRAILER:
                ArrayList<TrailerItem> trailersList = new ArrayList<>();
                try {
                    trailersList = getTrailerInfoFromJson(jsonStr);
                } catch (JSONException e) {
                    Log.v(LOG_TAG, "JSONException during parsing trailer info from json file");
                }

                ListView listView = (ListView)mActivity.findViewById(R.id.trailer_list_view);
                MovieTrailerAdapter trailerAdapter = new MovieTrailerAdapter(mContext, trailersList);
                listView.setAdapter(trailerAdapter);
                setListViewHeightBasedOnChildren(listView);
                break;
            case REVIEW:
               String reviewUrlStr = null;
                try {
                    reviewUrlStr = getReviewUrlStrFromJson(jsonStr);
                } catch (JSONException e) {
                    Log.v(LOG_TAG, "JSONException during parsing review url from json file");
                }
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(reviewUrlStr));
                mContext.startActivity(intent);
                break;
        }
    }

    //This method fetches useful data from the raw json file.
    private ArrayList<MovieItem> getMovieDataFromJson(String movieImageJsonStr) throws JSONException {
        final String TAG_RESULT = "results";
        final String TAG_POSTER_PATH = "poster_path";
        final String TAG_TITLE = "title";
        final String TAG_RELEASE_DATE = "release_date";
        final String TAG_VOTE_AVERAGE = "vote_average";
        final String TAG_SYNOPSIS = "overview";
        final String TAG_ID = "id";

        ArrayList<MovieItem> movieData = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(movieImageJsonStr);
        JSONArray resultArray = jsonObject.getJSONArray(TAG_RESULT);
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject currentObj = resultArray.getJSONObject(i);
            String posterPath = currentObj.getString(TAG_POSTER_PATH);
            String title = currentObj.getString(TAG_TITLE);
            String releaseDate = currentObj.getString(TAG_RELEASE_DATE);
            String voteAverage = currentObj.getString(TAG_VOTE_AVERAGE);
            String synopsis = currentObj.getString(TAG_SYNOPSIS);
            String id = currentObj.getString(TAG_ID);

            MovieItem item = new MovieItem(posterPath, title, releaseDate, voteAverage, synopsis, id);
            movieData.add(item);
        }
        return movieData;
    }

    //This method fetches movie trailer's Info from raw json string file.
    private ArrayList<TrailerItem> getTrailerInfoFromJson(String jsonStr) throws JSONException{
        final String TAG_RESULT = "results";
        final String TAG_KEY = "key";
        final String TAG_NAME = "name";

        ArrayList<TrailerItem> trailerData = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray resultArray = jsonObject.getJSONArray(TAG_RESULT);
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject currentObj = resultArray.getJSONObject(i);
            String key = currentObj.getString(TAG_KEY);
            Log.v("trailerInfo", key);
            String name = currentObj.getString(TAG_NAME);
            Log.v("trailerInfo", name);

            TrailerItem item = new TrailerItem(key, name);
            trailerData.add(item);
        }
        return trailerData;
    }

    //This method fetches the url string of the movie's review from raw json string.
    private String getReviewUrlStrFromJson(String jsonStr) throws JSONException{
        final String TAG_RESULT = "results";
        final String TAG_URL = "url";

        JSONObject jsonObject = new JSONObject(jsonStr);
        String reviewUrlStr = null;
        JSONArray resultArray = jsonObject.getJSONArray(TAG_RESULT);
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject currentObj = resultArray.getJSONObject(i);
            reviewUrlStr = currentObj.getString(TAG_URL);
        }
        return reviewUrlStr;
    }

    //This method fetches json string from a specific url
    private String getJsonStrFromUrl (URL url) {
        String mJsonStr = "";
        HttpURLConnection httpURLConnection = null;
        InputStream is = null;

        //Check if the url is null which is a invalid url.
        if (url == null) {
            return mJsonStr;
        }

        try {
            //Create the request to Movie DB and open the connection.
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(5000/*milliseconds*/);
            httpURLConnection.setConnectTimeout(7000/*milliseconds*/);
            httpURLConnection.connect();

            //Read the input stream from httpConnection if the request is successful.
            if (httpURLConnection.getResponseCode() == 200) {
                is = httpURLConnection.getInputStream();
                mJsonStr = getStringFromInputStream(is);
            }
        } catch (IOException e) {
            Log.v(LOG_TAG, "IOException occurred during opening the url");
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return mJsonStr;
    }

    //This method reads string from an inputStream.
    private String getStringFromInputStream (InputStream is) {
        String string = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                line = line + "\n";
                sb.append(line);
            }
            string = sb.toString();
        } catch (IOException e) {
            Log.v(LOG_TAG, e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream");
                }
            }
        }
        return string;
    }

    //This method returns Url determined by query type.
    private URL getUrl(InfoType type, String id) throws MalformedURLException{
        final String MOVIE_SEARCH_BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String TAG_POPULAR = "popular";
        final String TAG_TOP_RATED = "top_rated";
        final String TAG_API_KEY = "?api_key=";
        final String TAG_TRAILER = "/videos";
        final String TAG_REVIEW = "/reviews";

        //Create api url.
        URL url = null;

        switch (type) {
            case GENERAL:
                switch (mItemType) {
                    case POPULAR:
                    url = new URL(MOVIE_SEARCH_BASE_URL + TAG_POPULAR + TAG_API_KEY + BuildConfig.MOVIE_DB_API_KEY);
                        break;
                    case TOP_RATE:
                    url = new URL(MOVIE_SEARCH_BASE_URL + TAG_TOP_RATED + TAG_API_KEY + BuildConfig.MOVIE_DB_API_KEY);
                        break;
                }
                break;
            case TRAILER:
                url = new URL(MOVIE_SEARCH_BASE_URL + id + TAG_TRAILER + TAG_API_KEY + BuildConfig.MOVIE_DB_API_KEY);
                break;
            case REVIEW:
                url = new URL(MOVIE_SEARCH_BASE_URL + id + TAG_REVIEW + TAG_API_KEY + BuildConfig.MOVIE_DB_API_KEY);
                break;
        }

        return url;
    }

    //This method is used to auto-adjust the height of listView.
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
