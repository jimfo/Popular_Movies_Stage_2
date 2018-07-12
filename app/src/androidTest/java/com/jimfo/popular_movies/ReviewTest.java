package com.jimfo.popular_movies;

import com.jimfo.popular_movies.model.Review;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ReviewTest {

    private static final ArrayList<Review> REVIEWS = new ArrayList<>();

    private static final String REVIEW_ID = "1";
    private static final String MOVIE_ID = "11";
    private static final String REVIEW_AUTHOR = "John Smith";
    private static final String REVIEW_CONTENT = "Good Movie";

    static {
        REVIEWS.add(new Review("1", "11", "Bob Smith", "Bad Movie"));
        REVIEWS.add(new Review("2", "22", "John Smith", "Good Movie"));
    }

    @Test
    public void testGetters() {
        assertEquals(MOVIE_ID, REVIEWS.get(0).getmMovieId());
        assertEquals(REVIEW_ID, REVIEWS.get(0).getmReviewId());
        assertEquals(REVIEW_AUTHOR, REVIEWS.get(1).getmAuthor());
        assertEquals(REVIEW_CONTENT, REVIEWS.get(1).getmContent());
    }
}
