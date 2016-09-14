package com.sam_chordas.android.stockhawk.data;

/**
 * Created by Zang on 2016-09-14.
 */
public class Stock {
    private String mSymbol;
    private String mDate;
    private String mOpen;
    private String mHigh;
    private String mLow;
    private String mClose;
    private String mVolume;
    private String mAdj_Close;
    private String mBid_Price;
    private String mChange;
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
