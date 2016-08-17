package com.zangfengshun.popmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private boolean mIsMostPopular = true;

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
                break;
            case R.id.highest_rated:
                mIsMostPopular = false;
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
        new FetchMoviesData(this, this, mIsMostPopular, FetchMoviesData.InfoType.GENERAL).execute();
    }

}
