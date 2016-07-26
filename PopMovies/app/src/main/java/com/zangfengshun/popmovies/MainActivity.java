package com.zangfengshun.popmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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

public class MainActivity extends AppCompatActivity {
    private MovieInfoAdapter mImageAdapter;
    private ArrayList<MovieItem> mMovieInfo;
    private boolean mIsMostPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                break;
            case R.id.highest_rated:
                mIsMostPopular = false;
                updateMoviesImage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMoviesImage();
    }

    //This method is used to update movies image.
    private void updateMoviesImage() {
        new FetchMoviesData(this, mIsMostPopular).execute();
    }

    private class FetchMoviesData extends AsyncTask<String, Void, ArrayList<MovieItem>> {
        private final String LOG_TAG = FetchMoviesData.class.getSimpleName();
        private Context mContext;
        private boolean mIsMostPopular;

        public FetchMoviesData(Context context, boolean isMostPopular) {
            mContext = context;
            mIsMostPopular = isMostPopular;
        }

        //This method is used to fetch raw string data and then parse it into custom movie info from the movie data base.
        @Override
        protected ArrayList<MovieItem> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;

            //Will contain the raw JSON response as a string.
            String movieDataJsonStr = null;

            try {
                final String MOVIE_RESEARCH_BASE_URL = "https://api.themoviedb.org/3/movie/";
                final String TAG_POPULAR = "popular?";
                final String TAG_TOP_RATED = "top_rated?";
                final String TAG_API_KEY = "api_key=";

                //Create api url.
                URL url;
                if (mIsMostPopular) {
                    url = new URL(MOVIE_RESEARCH_BASE_URL + TAG_POPULAR + TAG_API_KEY + BuildConfig.MOVIE_DB_API_KEY);
                } else {
                    url = new URL(MOVIE_RESEARCH_BASE_URL + TAG_TOP_RATED + TAG_API_KEY + BuildConfig.MOVIE_DB_API_KEY);
                }

                Log.v(LOG_TAG, url.toString());

                //Create the request to Movie DB and open the connection.
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                //Read the input stream into a String.
                InputStream is = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line + "\n";
                    sb.append(line);
                }
                movieDataJsonStr = sb.toString();

            } catch (MalformedURLException e) {
                Log.v(LOG_TAG, "MalformedURLException");
            } catch (IOException e) {
                Log.v(LOG_TAG, "IOException occurred during opening the url");
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream");
                    }
                }
            }
            ArrayList<MovieItem> _movieImagePostPath = new ArrayList<>();
            try {
                _movieImagePostPath = getMovieDataFromJson(movieDataJsonStr);
            } catch (JSONException e) {
                Log.v(LOG_TAG, "JSONException during json data parsing");
            }
            return _movieImagePostPath;
        }

        //After getting data, this method shows images on main screen and handles item click event.
        @Override
        protected void onPostExecute(ArrayList<MovieItem> movieData) {
            super.onPostExecute(movieData);
            mMovieInfo = movieData;
            GridView gridView = (GridView) findViewById(R.id.main_activity_grid_view);
            mImageAdapter = new MovieInfoAdapter(mContext, mMovieInfo);
            gridView.setAdapter(mImageAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    MovieItem item = mImageAdapter.getItem(position);
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("item_info", item);
                    startActivity(intent);
                }
            });
        }

        //This method fetches useful data from the raw json file.
        private ArrayList<MovieItem> getMovieDataFromJson(String movieImageJsonStr) throws JSONException {
            final String TAG_RESULT = "results";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_TITLE = "title";
            final String TAG_RELEASE_DATE = "release_date";
            final String TAG_VOTE_AVERAGE = "vote_average";
            final String TAG_SYNOPSIS = "overview";

            ArrayList<MovieItem> movieImagePosterPath = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(movieImageJsonStr);
            JSONArray resultArray = jsonObject.getJSONArray(TAG_RESULT);
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject currentObj = resultArray.getJSONObject(i);
                String posterPath = currentObj.getString(TAG_POSTER_PATH);
                String title = currentObj.getString(TAG_TITLE);
                String releaseDate = currentObj.getString(TAG_RELEASE_DATE);
                String voteAverage = currentObj.getString(TAG_VOTE_AVERAGE);
                String synopsis = currentObj.getString(TAG_SYNOPSIS);

                MovieItem item = new MovieItem(posterPath, title, releaseDate, voteAverage, synopsis);
                movieImagePosterPath.add(item);
            }
            return movieImagePosterPath;
        }
    }
}
