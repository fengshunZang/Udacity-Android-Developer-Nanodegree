package com.zangfengshun.popmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane = false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    //private FetchMoviesData.OptionsItemType mItemType = FetchMoviesData.OptionsItemType.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            MovieDisplayFragment displayFragment = (MovieDisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movie_list);
            mTwoPane = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean("twoPane", mTwoPane);
            displayFragment.setArguments(bundle);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.most_popular:
//                mItemType = FetchMoviesData.OptionsItemType.POPULAR;
//                updateMoviesImage();
//                break;
//            case R.id.highest_rated:
//                mItemType = FetchMoviesData.OptionsItemType.TOP_RATE;
//                updateMoviesImage();
//                break;
//            case R.id.favorite:
//                mItemType = FetchMoviesData.OptionsItemType.FAVORITE;
//                updateMoviesImage();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
