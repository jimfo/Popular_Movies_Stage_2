package com.jimfo.popular_movies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String mReviewId;

    private String mMovieId;

    private String mAuthor;

    private String mContent;

    public Review() {
    }

    public Review(String reviewId, String movieId, String author, String content) {
        this.mReviewId = reviewId;
        this.mMovieId = movieId;
        this.mAuthor = author;
        this.mContent = content;
    }

    public Review(String movieId, String content) {
        this.mMovieId = movieId;
        this.mContent = content;
    }

    public String getmReviewId() {
        return mReviewId;
    }

    public void setmReviewId(String mReviewId) {
        this.mReviewId = mReviewId;
    }

    public String getmMovieId() {
        return mMovieId;
    }

    public void setmMovieId(String mMovieId) {
        this.mMovieId = mMovieId;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    @Override
    public String toString() {
        if (mAuthor == null) {
            return "REVIEW : " + mContent + "\n\n";
        } else {
            return "AUTHOR : " + mAuthor + "\n" + "REVIEW : " + mContent + "\n\n";
        }
    }

    protected Review(Parcel in) {
        mReviewId = in.readString();
        mMovieId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mReviewId);
        dest.writeString(mMovieId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
