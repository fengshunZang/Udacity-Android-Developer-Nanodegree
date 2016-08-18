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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new MovieDetailFragment())
                    .commit();
        }
    }
}
