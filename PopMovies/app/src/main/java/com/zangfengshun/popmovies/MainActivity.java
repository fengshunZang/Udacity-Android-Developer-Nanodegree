package com.zangfengshun.popmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private FetchMoviesData.OptionsItemType mItemType = FetchMoviesData.OptionsItemType.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                mItemType = FetchMoviesData.OptionsItemType.POPULAR;
                updateMoviesImage();
                break;
            case R.id.highest_rated:
                mItemType = FetchMoviesData.OptionsItemType.TOP_RATE;
                updateMoviesImage();
                break;
            case R.id.favorite:
                mItemType = FetchMoviesData.OptionsItemType.FAVORITE;
                updateMoviesImage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMoviesImage();
    }

    //This method is used to update movies image.
    private void updateMoviesImage() {
        new FetchMoviesData(this, this, mItemType, FetchMoviesData.InfoType.GENERAL).execute();
    }

}
