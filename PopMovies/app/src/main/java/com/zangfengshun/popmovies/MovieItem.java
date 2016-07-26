package com.zangfengshun.popmovies;

import java.io.Serializable;

/**
 * Created by Zang on 2016-07-25.
 * This class contains all useful information of any selected movie item. These information includes
 * image path, title, release data, average vote and overview.
 */
public class MovieItem implements Serializable {
    private String mMovieImagePath;
    private String mTitle;
    private String mReleaseDate;
    private String mVoteAverage;
    private String mPlotSynopsis;

    public MovieItem(String movieImagePath, String title,
                     String releaseDate, String voteAverage, String plotSynopsis) {
        mMovieImagePath = movieImagePath;
        mTitle = title;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mPlotSynopsis = plotSynopsis;
    }

    public String getMovieImagePath() {
        return mMovieImagePath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

}
