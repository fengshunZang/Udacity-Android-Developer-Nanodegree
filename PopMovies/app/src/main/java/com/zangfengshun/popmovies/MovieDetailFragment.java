package com.zangfengshun.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private String mCurrentItemId;
    private MovieDbHelper mDbHelper;
    private MovieItem mItemInfo;
    private Context mContext;

    private TextView mTitle;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mAverageVote;
    private TextView mPlotSynopsis;
    private TextView mButtonReview;
    private TextView mButtonFavorite;
    private ListView mListView;

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

//        mItemInfo = getArguments().getParcelable("movie_item_key");
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mItemInfo = intent.getParcelableExtra("par_key");
        mCurrentItemId = mItemInfo.getID();


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
    }
}
