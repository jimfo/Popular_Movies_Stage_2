package com.jimfo.popular_movies.model;

public class Trailer {

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
}
