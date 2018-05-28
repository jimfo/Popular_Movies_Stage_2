package com.jimfo.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable {

    private String mTitle;
    private String mId;
    private String mReleaseDate;
    private String mVoteAverage;
    private String mPlotSummary;
    private String mMoviePoster;
    private String mBackdrop;
    private String mLanguage;

    public Film() {

    }

    public Film(String title, String id, String releaseDate, String voteAverage, String plot,
                String image, String backDrop, String language) {
        this.mTitle = title;
        this.mId = id;
        this.mReleaseDate = releaseDate;
        this.mVoteAverage = voteAverage;
        this.mPlotSummary = plot;
        this.mMoviePoster = image;
        this.mBackdrop    = backDrop;
        this.mLanguage    = language;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String id) {
        this.mId = id;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmPlotSummary() {
        return mPlotSummary;
    }

    public void setmPlotSummary(String mPlotSummary) {
        this.mPlotSummary = mPlotSummary;
    }

    public String getmMoviePoster() {
        return mMoviePoster;
    }

    public void setmMoviePoster(String mMoviewPoster) {
        this.mMoviePoster = mMoviewPoster;
    }

    public String getmBackdrop() {
        return mBackdrop;
    }

    public void setmBackdrop(String mBackdrop) {
        this.mBackdrop = mBackdrop;
    }

    public String getmLanguage() {return mLanguage; }

    public void setmLanguage(String lang) { this.mLanguage = lang; }

    @Override
    public String toString() {
        return "TITLE : " + getmTitle();
    }

    // -------------- Parcelable Section -------------- //

    protected Film(Parcel in) {
        mTitle = in.readString();
        mId = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readString();
        mPlotSummary = in.readString();
        mMoviePoster = in.readString();
        mBackdrop    = in.readString();
        mLanguage    = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mId);
        dest.writeString(mReleaseDate);
        dest.writeString(mVoteAverage);
        dest.writeString(mPlotSummary);
        dest.writeString(mMoviePoster);
        dest.writeString(mBackdrop);
        dest.writeString(mLanguage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
}
