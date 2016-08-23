package com.zangfengshun.popmovies.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zang on 2016-08-03.
 */
public class TrailerItem implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TrailerItem> CREATOR = new Creator<TrailerItem>() {
        @Override
        public TrailerItem createFromParcel(Parcel source) {
            return new TrailerItem(source.readString(), source.readString());
        }

        @Override
        public TrailerItem[] newArray(int size) {
            return new TrailerItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mKey);
        parcel.writeString(mName);
    }
}
