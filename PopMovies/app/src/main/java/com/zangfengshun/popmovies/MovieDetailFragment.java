package com.zangfengshun.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zangfengshun.popmovies.adapter.MovieInfoAdapter;
import com.zangfengshun.popmovies.data.MovieDbHelper;
import com.zangfengshun.popmovies.item.MovieItem;

public class MovieDetailFragment extends Fragment {
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private String mCurrentItemId;
    private MovieDbHelper mDbHelper;
    private MovieItem mItemInfo;
    private Context mContext;
    private boolean mTwoPane;

    private TextView mTitle;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mAverageVote;
    private TextView mPlotSynopsis;
    private TextView mButtonReview;
    private TextView mButtonFavorite;
    private ListView mListView;
    private TextView mEmptyContentIndicator;
    private View mBlackLine;
    private TextView mTrailers;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mTitle = (TextView)rootView.findViewById(R.id.movie_title);
        mPoster = (ImageView)rootView.findViewById(R.id.movie_poster);
        mReleaseDate = (TextView)rootView.findViewById(R.id.movie_release_date);
        mAverageVote = (TextView)rootView.findViewById(R.id.movie_vote_average);
        mPlotSynopsis = (TextView)rootView.findViewById(R.id.movie_overview);
        mButtonReview = (TextView)rootView.findViewById(R.id.button_review);
        mButtonFavorite = (TextView)rootView.findViewById(R.id.button_favorite);
        mListView = (ListView)rootView.findViewById(R.id.trailer_list_view);
        mEmptyContentIndicator = (TextView)rootView.findViewById(R.id.empty_content_indicator);
        mBlackLine = rootView.findViewById(R.id.black_line);
        mTrailers = (TextView) rootView.findViewById(R.id.trailers);

        if (getActivity()instanceof DetailActivity) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }

        //If no item is selected from the left pane, a notice will show up.
        if (getArguments() != null) {
            mItemInfo = getArguments().getParcelable("movie_item");
            if (mItemInfo == null) {
                Log.v(LOG_TAG, "No movie data received in MovieDetailFragment.");
            } else {
                mCurrentItemId = mItemInfo.getID();
            }
            mEmptyContentIndicator.setVisibility(View.GONE);
        } else {
            mEmptyContentIndicator.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.GONE);
            mPoster.setVisibility(View.GONE);
            mReleaseDate.setVisibility(View.GONE);
            mAverageVote.setVisibility(View.GONE);
            mPlotSynopsis.setVisibility(View.GONE);
            mButtonFavorite.setVisibility(View.GONE);
            mButtonReview.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mBlackLine.setVisibility(View.GONE);
            mTrailers.setVisibility(View.GONE);
            return rootView;
        }

        mTitle.setText(mItemInfo.getTitle());
        String wholePath = MovieInfoAdapter.MOVIE_IMAGE_URL_PREFIX + mItemInfo.getMovieImagePath();
        Picasso.with(mContext).load(wholePath).into(mPoster);
        String releaseDateText = "Release Date: \n" + mItemInfo.getReleaseDate();
        mReleaseDate.setText(releaseDateText);
        String voteAverageText = "Average Vote: \n" + mItemInfo.getVoteAverage() + "/10";
        mAverageVote.setText(voteAverageText);
        mPlotSynopsis.setText(mItemInfo.getPlotSynopsis());

        new FetchMoviesData(getActivity(), getContext(), mListView, FetchMoviesData.InfoType.TRAILER, mCurrentItemId).execute();

        //Handle click event of review textView.
        mButtonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchMoviesData(getActivity(), getContext(), mListView, FetchMoviesData.InfoType.REVIEW, mCurrentItemId).execute();
            }
        });

        //Handle click event of favorite textView.
        mButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper = new MovieDbHelper(mContext);
                mDbHelper.addMovieEntry(mItemInfo);
                Toast toast = Toast.makeText(mContext, "Congrats! You've added a movie into your favorite list.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        return rootView;
    }
}
