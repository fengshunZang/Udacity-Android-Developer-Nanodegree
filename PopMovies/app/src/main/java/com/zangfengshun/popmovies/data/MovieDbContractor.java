package com.zangfengshun.popmovies.data;

import android.provider.BaseColumns;

/**
 * Created by Zang on 2016-08-17.
 * This is the contract for movie item table.
 */
public class MovieDbContractor {
    //Empty constructor
    public MovieDbContractor() {}

    /*
    Inner class defines the table content.
     */
    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie_inventory";
        public static final String COLUMN_NAME_MOVIE_IMAGE_PATH = "image_path";
        public static final String COLUMN_NAME_MOVIE_TITLE = "title";
        public static final String COLUMN_NAME_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_MOVIE_AVERAGE_VOTE = "average_vote";
        public static final String COLUMN_NAME_MOVIE_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_NAME_MOVIE_ID = "movie_id";
    }
}
