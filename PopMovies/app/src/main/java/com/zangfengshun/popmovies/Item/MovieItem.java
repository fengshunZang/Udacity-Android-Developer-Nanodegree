package com.zangfengshun.popmovies.Item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zang on 2016-07-25.
 * This class contains all useful information of any selected movie item. These information includes
 * image path, title, release data, average vote and overview.
 */
public class MovieItem implements Parcelable {
    private String mMovieImagePath;
    private String mTitle;
    private String mReleaseDate;
    private String mVoteAverage;
    private String mPlotSynopsis;
    private String mID;

    public MovieItem(String movieImagePath, String title,
                     String releaseDate, String voteAverage, String plotSynopsis, String ID) {
        mMovieImagePath = movieImagePath;
        mTitle = title;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mPlotSynopsis = plotSynopsis;
        mID = ID;
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

    public String getID() {
        return mID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source.readString(), source.readString(), source.readString(),
                    source.readString(), source.readString(), source.readString());
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mMovieImagePath);
        parcel.writeString(mTitle);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mVoteAverage);
        parcel.writeString(mPlotSynopsis);
        parcel.writeString(mID);
    }
}
