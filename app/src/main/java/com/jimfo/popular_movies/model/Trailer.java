package com.jimfo.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String mTrailerId;

    private String mMovieId;

    private String mKey;

    public Trailer(String trailerId, String movieId, String key) {
        this.mTrailerId = trailerId;
        this.mMovieId = movieId;
        this.mKey = key;
    }

    public String getmTrailerId() {
        return mTrailerId;
    }

    public void setmTrailerId(String mTrailerId) {
        this.mTrailerId = mTrailerId;
    }

    public String getmMovieId() {
        return mMovieId;
    }

    public void setmMovieId(String mMovieId) {
        this.mMovieId = mMovieId;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mName) {
        this.mKey = mName;
    }

    @Override
    public String toString() {
        return "TRAILER ID " + mTrailerId + "\n" +
                "MOVIE ID " + mMovieId + "\n" +
                "KEY " + mKey;
    }

    protected Trailer(Parcel in) {
        mTrailerId = in.readString();
        mMovieId = in.readString();
        mKey = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrailerId);
        dest.writeString(mMovieId);
        dest.writeString(mKey);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
