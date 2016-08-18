package com.zangfengshun.popmovies.item;

/**
 * Created by Zang on 2016-08-03.
 */
public class TrailerItem {
    private String mKey;
    private String mName;

    public TrailerItem(String key, String name) {
        mKey = key;
        mName = name;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

}
