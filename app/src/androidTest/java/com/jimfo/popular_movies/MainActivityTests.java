package com.jimfo.popular_movies;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static final String MOVIE_TITLE = "Jurassic World: Fallen Kingdom";

    /**
     * Clicks on a GridView item and checks it opens up the DetailActivity with the correct details.
     */
    @Test
    public void clickGridViewItemOpensDetailActivity() {

        onView(withId(R.id.rv_movies))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Checks that the DetailActivity opens with the correct movie name displayed
        onView(withId(R.id.movieTitle)).check(matches(withText(MOVIE_TITLE)));
    }
}
