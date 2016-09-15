package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.Stock;
import com.sam_chordas.android.stockhawk.data.StockDeserializer;
import com.sam_chordas.android.stockhawk.rest.StockDataPoint;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockDetailGraph extends AppCompatActivity {
    private final String LOG_TAG = StockDetailGraph.class.getSimpleName();
    private String mSymbol;
    private String mStartDate;
    private String mEndDate;
    private List<Stock> mItems;
    private Type mListType;
    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        Intent intent = getIntent();
        mSymbol = intent.getStringExtra(QuoteColumns.SYMBOL);
        Log.v(LOG_TAG, mSymbol);
        mListType = new TypeToken<List<Stock>>() {
        }.getType();

        //Set StartDate and EndDate of the line chart.
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mEndDate = simpleDateFormat.format(currentDate);
        cal.add(Calendar.MONTH, -1);
        mStartDate = simpleDateFormat.format(cal.getTime());

        Log.v("start date", mStartDate);
        Log.v("end date", mEndDate);

        //Inflate LineChart View.
        mLineChart = (LineChart) findViewById(R.id.chart);

//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Stock.class, new StockDeserializer())
//                .create();
        //Use Retrofit to establish HTTP connection to fetch stock data.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        mListType, new StockDeserializer()).create()))
                .build();
        StockDataPoint stockDataPoint = retrofit.create(StockDataPoint.class);

        String query = "select * from yahoo.finance.historicaldata where symbol = \'" + mSymbol + "\' and startDate = \'" + mStartDate + "\' and endDate = \'" + mEndDate + "\'";
        Call<List<Stock>> call = stockDataPoint.getData(query);
        call.enqueue(new Callback<List<Stock>>() {
            @Override
            public void onResponse(Call<List<Stock>> call, Response<List<Stock>> response) {
                Log.v(LOG_TAG, response.raw().toString());

                mItems = response.body();
//                if (mItems == null) {
//                    Log.v(LOG_TAG, "items are null");
//                } else {
//                    for(Stock item: mItems) {
//                        Log.v(LOG_TAG, item.getClose());
//                    }
//                }

                displayChart();
            }

            @Override
            public void onFailure(Call<List<Stock>> call, Throwable t) {
                Log.v(LOG_TAG, "Callback failed.");
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "No Infomation found", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayChart() {
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextSize(11f);

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        //Collections.reverse(mItems);
        Log.v(LOG_TAG, String.valueOf(mItems.size()));
        for (int i = 0; i < mItems.size(); i++) {
            xVals.add(i, mItems.get(i).getDate());
            Log.v(LOG_TAG, mItems.get(i).getDate());
            yVals.add(new Entry(Float.valueOf(mItems.get(i).getClose()), i));
        }

        LineDataSet dataSet = new LineDataSet(yVals, mSymbol);
        LineData lineData = new LineData(xVals, dataSet);
        mLineChart.setData(lineData);
        mLineChart.setDescription("Stock price over time.");
        mLineChart.setDescriptionTextSize(15f);
        mLineChart.getLegend().setTextSize(12f);
        mLineChart.setPinchZoom(false);
        mLineChart.invalidate();
    }
}
