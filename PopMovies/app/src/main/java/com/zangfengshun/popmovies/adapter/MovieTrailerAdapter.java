package com.zangfengshun.popmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zangfengshun.popmovies.item.TrailerItem;
import com.zangfengshun.popmovies.R;

import java.util.ArrayList;

/**
 * Created by Zang on 2016-08-03.
 */
public class MovieTrailerAdapter extends ArrayAdapter<TrailerItem> {
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private String mYoutubeUrl = null;

    public MovieTrailerAdapter(Context context, ArrayList<TrailerItem> objects) {
        super(context, 0, objects);
        Log.v("Trailer Adapter", "constructor");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }
        TrailerItem currentItem = getItem(position);
        mYoutubeUrl = YOUTUBE_BASE_URL + currentItem.getKey();
        Log.v("TrailerAdapter", mYoutubeUrl);

        ImageView trailerImage = (ImageView)listView.findViewById(R.id.trailer_player);
        trailerImage.setImageResource(R.drawable.play_button);
        trailerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(mYoutubeUrl));
                    getContext().startActivity(intent);
            }
        });

        TextView trailerName = (TextView)listView.findViewById(R.id.trailer_name);
        trailerName.setText(currentItem.getName());

        return listView;
    }
}
