package com.zangfengshun.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.zangfengshun.popmovies.adapter.MovieInfoAdapter;
import com.zangfengshun.popmovies.data.MovieItemFetcher;
import com.zangfengshun.popmovies.data.Utility;
import com.zangfengshun.popmovies.item.MovieItem;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class MovieDisplayFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<MovieItem>> {
    private static final String LOG_TAG = "MovieDisplayFragment";
    private static final int MOVIE_LOADER_ID = 0;
    private Utility.OptionsItemType mItemType = Utility.OptionsItemType.POPULAR;
    private GridView mGridView;
    private MovieInfoAdapter mImageAdapter;
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

        mGridView = (GridView) rootView.findViewById(R.id.main_activity_grid_view);

        //mImageAdapter = new MovieInfoAdapter(mContext, mMovieInfo);
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieItem item = mImageAdapter.getItem(position);
                //determine current state.
                mTwoPane = ((MainActivity)getActivity()).isTwoPane();
                if (!mTwoPane) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("par_key", item);
                    getActivity().startActivity(intent);
                    Log.v(LOG_TAG, "Sent intent to detailActivity.");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("movie_item", item);
                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                    movieDetailFragment.setArguments(bundle);
                    Log.v(LOG_TAG, "replace the container with a new fragment");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, movieDetailFragment).commit();
                }
            }
        });


        //Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        //updateMoviesImage();
        return rootView;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LoaderManager loaderManager = getLoaderManager();
        switch (item.getItemId()) {
            case R.id.most_popular:
                mItemType = Utility.OptionsItemType.POPULAR;
                loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
                break;
            case R.id.highest_rated:
                mItemType = Utility.OptionsItemType.TOP_RATE;
                loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
                break;
            case R.id.favorite:
                mItemType = Utility.OptionsItemType.FAVORITE;
                loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    //This method is used to update movies image.
//    public void updateMoviesImage() {
//        new FetchMoviesData(getActivity(), getContext(), mGridView, mItemType, FetchMoviesData.InfoType.GENERAL).execute();
//    }

    @Override
    public Loader<List<MovieItem>> onCreateLoader(int id, Bundle args) {
        return new MovieItemFetcher(getContext(), Utility.InfoType.GENERAL, mItemType);
    }

    @Override
    public void onLoadFinished(Loader<List<MovieItem>> loader, List<MovieItem> data) {
        mImageAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mImageAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieItem>> loader) {
        mImageAdapter.clear();
    }
}
