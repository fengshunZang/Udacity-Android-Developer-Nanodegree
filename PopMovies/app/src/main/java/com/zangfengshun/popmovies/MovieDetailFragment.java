package com.zangfengshun.popmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ScrollingView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zangfengshun.popmovies.adapter.MovieInfoAdapter;
import com.zangfengshun.popmovies.adapter.MovieTrailerAdapter;
import com.zangfengshun.popmovies.data.MovieDbHelper;
import com.zangfengshun.popmovies.data.Utility;
import com.zangfengshun.popmovies.fetcher.MovieReviewFetcher;
import com.zangfengshun.popmovies.fetcher.MovieTrailerFetcher;
import com.zangfengshun.popmovies.item.MovieItem;
import com.zangfengshun.popmovies.item.TrailerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;
    private String mCurrentItemId;
    private MovieDbHelper mDbHelper;
    private MovieItem mItemInfo;
    private ArrayList<TrailerItem> mMovieTrailerList;
    private String mReviewContent;
    private Context mContext;
    private Utility.OptionsItemType mItemType = Utility.OptionsItemType.POPULAR;
    @BindView(R.id.movie_title)
    TextView mTitle;
    @BindView(R.id.movie_poster)
    ImageView mPoster;
    @BindView(R.id.movie_release_date)
    TextView mReleaseDate;
    @BindView(R.id.movie_vote_average)
    TextView mAverageVote;
    @BindView(R.id.movie_overview)
    TextView mPlotSynopsis;
    @BindView(R.id.button_unfavorite)
    TextView mButtonUnfavorite;
    @BindView(R.id.button_favorite)
    TextView mButtonFavorite;
    @BindView(R.id.trailer_list_view)
    ListView mListView;
    @BindView(R.id.empty_content_indicator)
    TextView mEmptyContentIndicator;
    private View mBlackLine;
    private TextView mTrailers;
    @BindView(R.id.scrollView)
    FrameLayout mScollingView;
    @BindView(R.id.reviews_content)
    TextView mReview;

    private MovieTrailerAdapter mListAdapter;

    //Getting two LoaderCallbacks implementations.
    private LoaderManager.LoaderCallbacks<ArrayList<TrailerItem>> mTrailerLoaderListener
            = new LoaderManager.LoaderCallbacks<ArrayList<TrailerItem>>() {
        @Override
        public void onLoaderReset(Loader<ArrayList<TrailerItem>> loader) {
            mListAdapter.clear();
        }

        @Override
        public Loader<ArrayList<TrailerItem>> onCreateLoader(int id, Bundle ags) {
            return new MovieTrailerFetcher(getContext(), Utility.InfoType.TRAILER, mItemType, mCurrentItemId);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<TrailerItem>> loader, ArrayList<TrailerItem> data) {
            mListAdapter.clear();
            mMovieTrailerList = data;

            if (data != null && !data.isEmpty()) {
                mListAdapter.addAll(mMovieTrailerList);
                setListViewHeightBasedOnChildren(mListView);
            }

        }
    };

    private LoaderManager.LoaderCallbacks<String> mReviewLoaderListener
            = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return new MovieReviewFetcher(getContext(), Utility.InfoType.REVIEW, mItemType, mCurrentItemId);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            mReview.setText(data);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

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
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //Getting views' objects
        ButterKnife.bind(this, rootView);
        //Initializing the DbHelper.
        mDbHelper = new MovieDbHelper(mContext);

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
            mScollingView.setVisibility(View.GONE);
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

        mListAdapter = new MovieTrailerAdapter(getContext(), new ArrayList<TrailerItem>());
        mListView.setAdapter(mListAdapter);
        setListViewHeightBasedOnChildren(mListView);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(TRAILER_LOADER_ID, null, mTrailerLoaderListener);
        loaderManager.initLoader(REVIEW_LOADER_ID, null, mReviewLoaderListener);

        //Handle click event of review textView.
        mButtonUnfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper.deleteMovieItem(mItemInfo);
                Toast toast = Toast.makeText(mContext, "Oops, you've deleted that movie from your favorite list.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        //Handle click event of favorite textView.
        mButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper.addMovieEntry(mItemInfo);
                Toast toast = Toast.makeText(mContext, "Congrats! You've added a movie into your favorite list.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        return rootView;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movie_trailer_list", mMovieTrailerList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieTrailerList = savedInstanceState.getParcelableArrayList("movie_trailer_list");
            mListAdapter.notifyDataSetChanged();
            ;
        }
    }
}
