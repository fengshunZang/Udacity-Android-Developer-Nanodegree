package com.zangfengshun.popmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zangfengshun.popmovies.adapter.MovieInfoAdapter;
import com.zangfengshun.popmovies.data.MovieDbHelper;
import com.zangfengshun.popmovies.item.MovieItem;

/**
 * Created by Zang on 2016-07-25.
 * The detail activity receives the intent and extra data of one movie item and shows them properly.
 */
public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private String mCurrentItemId;
    private MovieDbHelper mDbHelper = new MovieDbHelper(this);
    private MovieItem mItemInfo;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mItemInfo = intent.getParcelableExtra("par_key");
        mCurrentItemId = mItemInfo.getID();

        TextView title = (TextView) findViewById(R.id.movie_title);
        title.setText(mItemInfo.getTitle());

        ImageView poster = (ImageView) findViewById(R.id.movie_poster);
        String wholePath = MovieInfoAdapter.MOVIE_IMAGE_URL_PREFIX + mItemInfo.getMovieImagePath();
        Picasso.with(this).load(wholePath).into(poster);

        TextView releaseDate = (TextView) findViewById(R.id.movie_release_date);
        String releaseDateText = "Release Date: \n" + mItemInfo.getReleaseDate();
        releaseDate.setText(releaseDateText);

        TextView voteAverage = (TextView) findViewById(R.id.movie_vote_average);
        String voteAverageText = "Average Vote: \n" + mItemInfo.getVoteAverage() + "/10";
        voteAverage.setText(voteAverageText);

        TextView plotSynopsis = (TextView) findViewById(R.id.movie_overview);
        plotSynopsis.setText(mItemInfo.getPlotSynopsis());

        new FetchMoviesData(DetailActivity.this, DetailActivity.this, FetchMoviesData.InfoType.TRAILER, mCurrentItemId).execute();

        //Handle click event of review textView.
        TextView buttonReview = (TextView)findViewById(R.id.button_review);
        buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchMoviesData(DetailActivity.this, DetailActivity.this, FetchMoviesData.InfoType.REVIEW, mCurrentItemId).execute();
            }
        });

        //Handle click event of favorite textView.
        TextView buttonFavorite = (TextView)findViewById(R.id.button_favorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper.addMovieEntry(mItemInfo);
                Toast toast = Toast.makeText(mContext, "Congrats! You've added a movie into your favorite list.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
}
