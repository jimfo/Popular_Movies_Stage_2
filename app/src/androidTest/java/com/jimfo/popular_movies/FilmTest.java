package com.jimfo.popular_movies;

import com.jimfo.popular_movies.model.Film;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;

public class FilmTest {

    private static final ArrayList<Film> FILMS = new ArrayList<>();
    private static final String MOVIE_ID = "1";
    private static final String MOVIE_TITLE = "Movie_One";
    private static final String MOVIE_RELEASE = "02/02/2018";
    private static final String MOVIE_VOTE = "6.6";
    private static final String MOVIE_PLOT = "A movie about TWO";
    private static final String MOVIE_POSTER = "Image Location One";
    private static final String MOVIE_BACKDROP = "Backdrop location One";


    static{
        FILMS.add(new Film("1", "Movie_One", "01/01/2018","5.5","A movie about ONE","Image Location One",
                "Backdrop location One","English"));
        FILMS.add(new Film("2", "Movie_Two", "02/02/2018", "6.6", "A movie about TWO", "Image location Two",
                "Backdrop location Two", "English"));
    }

    @Test
    public void testGetters() {
        assertEquals(MOVIE_ID, FILMS.get(0).getmId());
        assertEquals(MOVIE_TITLE, FILMS.get(0).getmTitle());
        assertEquals(MOVIE_RELEASE, FILMS.get(1).getmReleaseDate());
        assertEquals(MOVIE_VOTE, FILMS.get(1).getmVoteAverage());
        assertEquals(MOVIE_PLOT, FILMS.get(1).getmPlotSummary());
        assertEquals(MOVIE_POSTER, FILMS.get(0).getmMoviePoster());
        assertEquals(MOVIE_BACKDROP, FILMS.get(0).getmBackdrop());
    }
}
