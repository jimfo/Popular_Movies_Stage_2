package com.jimfo.popular_movies.model;

public class Review {

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
}
