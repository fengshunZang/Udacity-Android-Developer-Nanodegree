package com.zangfengshun.popmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   private boolean mTwoPane = false;
    //private FetchMoviesData.OptionsItemType mItemType = FetchMoviesData.OptionsItemType.POPULAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            //MovieDisplayFragment displayFragment = (MovieDisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movie_list);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment())
                        .commit();
            }
        }
    }


    public boolean isTwoPane() {
       return mTwoPane;
   }

    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
