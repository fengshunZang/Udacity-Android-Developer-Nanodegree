package com.example.sam_chordas.stockhawk;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.WidgetDataProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by Zang on 2016-09-16.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
        return new WidgetDataProvider(this, intent);

    }
}
