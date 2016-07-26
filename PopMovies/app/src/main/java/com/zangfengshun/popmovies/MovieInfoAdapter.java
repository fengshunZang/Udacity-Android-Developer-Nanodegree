package com.zangfengshun.popmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Zang on 2016-07-25.
 * This adapter gets image information from the general movie item data.
 */
public class MovieInfoAdapter extends ArrayAdapter<MovieItem> {
    public static final String MOVIE_IMAGE_URL_PREFIX = "http://image.tmdb.org/t/p/w185/";
    private static final String LOG_TAG = MovieInfoAdapter.class.getSimpleName();

    public MovieInfoAdapter(Context context, ArrayList<MovieItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        MovieItem currentItem = getItem(position);
        String wholePath = MOVIE_IMAGE_URL_PREFIX + currentItem.getMovieImagePath();
        Log.v(LOG_TAG, wholePath);

        ImageView movieImage = (ImageView)convertView.findViewById(R.id.movie_poster_img);
        Picasso.with(getContext()).load(wholePath).into(movieImage);

        return convertView;
    }
}
