package com.zangfengshun.popmovies.data;

import android.util.Log;

import com.zangfengshun.popmovies.BuildConfig;
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
 * Created by Zang on 2016-08-21.
 */
public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();
    public enum InfoType {GENERAL, TRAILER, REVIEW}
    public enum OptionsItemType {POPULAR, TOP_RATE, FAVORITE}

    //This method returns Url determined by query type.
    public static URL getUrl(InfoType type, OptionsItemType itemType, String id) throws MalformedURLException {
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
                switch (itemType) {
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
                String reviewUrl = MOVIE_SEARCH_BASE_URL + id + TAG_REVIEW + TAG_API_KEY + BuildConfig.MOVIE_DB_API_KEY;
                url = new URL(reviewUrl);
                Log.v(LOG_TAG, "Movie Review URL is: " + reviewUrl);
                break;
        }

        return url;
    }

    //This method fetches json string from a specific url
    public static String getJsonStrFromUrl(URL url) {
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
    private static String getStringFromInputStream(InputStream is) {
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


    //This method fetches useful data from the raw json file.
    public static ArrayList<MovieItem> getMovieDataFromJson(String movieImageJsonStr) throws JSONException {
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
    public static ArrayList<TrailerItem> getTrailerInfoFromJson(String jsonStr) throws JSONException {
        final String TAG_RESULT = "results";
        final String TAG_KEY = "key";
        final String TAG_NAME = "name";

        ArrayList<TrailerItem> trailerData = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray resultArray = jsonObject.getJSONArray(TAG_RESULT);
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject currentObj = resultArray.getJSONObject(i);
            String key = currentObj.getString(TAG_KEY);
            String name = currentObj.getString(TAG_NAME);

            TrailerItem item = new TrailerItem(key, name);
            trailerData.add(item);
        }
        return trailerData;
    }

    //This method fetches the url string of the movie's review from raw json string.
    public static String getReviewStrFromJson(String jsonStr) throws JSONException {
        final String TAG_RESULT = "results";
        final String TAG_CONTENT = "content";

        JSONObject jsonObject = new JSONObject(jsonStr);
        String reviewStr = null;
        JSONArray resultArray = jsonObject.getJSONArray(TAG_RESULT);
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject currentObj = resultArray.getJSONObject(i);
            reviewStr = currentObj.getString(TAG_CONTENT);
        }
        return reviewStr;
    }
}
