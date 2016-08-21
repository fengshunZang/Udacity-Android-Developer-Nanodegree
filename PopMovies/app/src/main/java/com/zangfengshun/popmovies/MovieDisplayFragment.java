package com.zangfengshun.popmovies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.zip.Inflater;


public class MovieDisplayFragment extends Fragment {
    private static final String LOG_TAG = "MovieDisplayFragment";
    private FetchMoviesData.OptionsItemType mItemType = FetchMoviesData.OptionsItemType.POPULAR;
    private GridView mGridView;
    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_display, container, false);
        mTwoPane = ((MainActivity) getActivity()).isTwoPane();

        mGridView = (GridView) rootView.findViewById(R.id.main_activity_grid_view);
        updateMoviesImage();
        return rootView;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        updateMoviesImage();
//    }

    //This method is used to update movies image.
    public void updateMoviesImage() {
        new FetchMoviesData(getActivity(), getContext(), mGridView, mTwoPane, mItemType, FetchMoviesData.InfoType.GENERAL).execute();
    }
}
