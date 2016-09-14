package com.sam_chordas.android.stockhawk.rest;

import com.sam_chordas.android.stockhawk.data.Stock;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zang on 2016-09-14.
 */
public interface StockDataPoint {

    @GET("v1/public/yql?&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    Call<List<Stock>> getData(
            @Query("q") String query);
}
