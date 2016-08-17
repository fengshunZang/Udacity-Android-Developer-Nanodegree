package com.zangfengshun.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.zangfengshun.popmovies.Adapter.MovieInfoAdapter;
import com.zangfengshun.popmovies.Item.MovieItem;

/**
 * Created by Zang on 2016-07-25.
 * The detail activity receives the intent and extra data of one movie item and shows them properly.
 */
public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private String mCurrentItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        MovieItem mItemInfo = intent.getParcelableExtra("par_key");
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

        TextView buttonReview = (TextView)findViewById(R.id.button_review);
        buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchMoviesData(DetailActivity.this, DetailActivity.this, FetchMoviesData.InfoType.REVIEW, mCurrentItemId).execute();
            }
        });
    }
}
