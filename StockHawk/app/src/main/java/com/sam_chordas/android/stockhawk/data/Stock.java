package com.sam_chordas.android.stockhawk.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zang on 2016-09-14.
 */
public class Stock {
    @SerializedName("Symbol")
    private String mSymbol;
    @SerializedName("Date")
    private String mDate;
    @SerializedName("Open")
    private String mOpen;
    @SerializedName("High")
    private String mHigh;
    @SerializedName("Low")
    private String mLow;
    @SerializedName("Close")
    private String mClose;
    @SerializedName("Volume")
    private String mVolume;
    @SerializedName("Adj_Close")
    private String mAdj_Close;
    @SerializedName("Bid_Price")
    private String mBid_Price;
    @SerializedName("Change")
    private String mChange;
    @SerializedName("Percent_Change")
    private String mPercent_Change;

    public Stock(String symbol, String date, String open, String high, String low, String close,
                 String volume, String adj_close, String bid_price, String change, String percent_change) {
        this.mSymbol = symbol;
        this.mDate = date;
        this.mOpen = open;
        this.mHigh = high;
        this.mLow = low;
        this.mClose = close;
        this.mVolume = volume;
        this.mAdj_Close = adj_close;
        this.mBid_Price = bid_price;
        this.mChange = change;
        this.mPercent_Change = percent_change;
    }

    //Empty constructor
    public Stock() {}

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String symbol) {
        this.mSymbol = symbol;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getOpen() {
        return mOpen;
    }

    public void setOpen(String open) {
        this.mOpen = open;
    }

    public String getHigh() {
        return mHigh;
    }

    private void setHigh(String high) {
        this.mHigh = high;
    }

    public String getLow() {
        return mLow;
    }

    public void setLow(String low) {
        this.mLow = low;
    }

    public String getClose() {
        return mClose;
    }

    public void setClose(String close) {
        this.mClose = close;
    }

    public String getVolume() {
        return mVolume;
    }

    public void setVolume(String volume) {
        this.mVolume = volume;
    }

    public String getAdj_Close() {
        return mAdj_Close;
    }

    public void setAdj_Close(String adj_close) {
        this.mAdj_Close = adj_close;
    }

    public String getBid_Price() {
        return mBid_Price;
    }

    public void setBid_Price(String bid_price) {
        this.mBid_Price = bid_price;
    }

    public String getChange() {
        return mChange;
    }

    public void setChange(String change) {
        this.mChange = change;
    }

    public String getPercent_Change() {
        return mPercent_Change;
    }

    public void setPercent_Change(String percent_change) {
        this.mPercent_Change = percent_change;
    }
}
