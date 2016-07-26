package com.zangfengshun.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by Zang on 2016-07-25.
 * The detail activity receives the intent and extra data of one movie item and shows them properly.
 */
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        MovieItem mItemInfo = (MovieItem) intent.getSerializableExtra("item_info");

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
    }
}
