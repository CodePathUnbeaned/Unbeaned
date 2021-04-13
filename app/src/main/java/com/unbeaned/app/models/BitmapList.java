package com.unbeaned.app.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class BitmapList implements Parcelable {
    private List<Bitmap> images;

    public BitmapList() {
        images = new ArrayList<>();
    }

    protected BitmapList(Parcel in) {
        images = in.createTypedArrayList(Bitmap.CREATOR);
    }

    public static final Creator<BitmapList> CREATOR = new Creator<BitmapList>() {
        @Override
        public BitmapList createFromParcel(Parcel in) {
            return new BitmapList(in);
        }

        @Override
        public BitmapList[] newArray(int size) {
            return new BitmapList[size];
        }
    };

    public void addImage(Bitmap image) {
        images.add(image);
    }

    public List<Bitmap> getImages() {
        return images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(images);
    }
}
