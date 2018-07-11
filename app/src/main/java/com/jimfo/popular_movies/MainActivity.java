package com.jimfo.popular_movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jimfo.popular_movies.adapter.MovieAdapterRV;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapterRV.ItemClickListener,
        MovieTask.PostExecuteListener {

    private static String POSITION = "position";

    public MovieAdapterRV mAdapter;
    public RecyclerView mRecyclerView;
    private ArrayList<Film> mFilms;
    private TextView emptyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            View decor = getWindow().getDecorView();
            fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
            setTitle(this.getResources().getString(R.string.flix));
        }

        mRecyclerView = findViewById(R.id.rv_movies);
        LinearLayout refreshLL = findViewById(R.id.refreshbar_ll);
        refreshLL.setBackgroundColor(getResources().getColor(R.color.ll_color));
        emptyTV = findViewById(R.id.empty_view);

        // Layout determined by orientation
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        if(savedInstanceState == null){

            if (NetworkUtils.isNetworkAvailable(this.getApplicationContext())) {

                // if db is empty check if network is available and get movies from TMDB
                getMovies(getString(R.string.popular));

            }
            else {
                new MovieTask(this, this).execute(getResources().getString(R.string.favorite));
            }
        }
        else {

                Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(POSITION);
                mFilms = savedInstanceState.getParcelableArrayList("key");
                mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
                updateAdapter();

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(POSITION, mRecyclerView.getLayoutManager().onSaveInstanceState());
        savedInstanceState.putParcelableArrayList("key", mFilms);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(POSITION);
        mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }

    /**
     * Purpose : Inflates the settings.xml file
     *
     * @param menu : settings.xml
     * @return : true
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    /**
     * Purpose : Setup call for getMovies method
     *
     * @param item : The item selected from the Menu options
     * @return : true
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {

            case R.id.top_rated:
                getMovies(this.getResources().getString(R.string.top_rated));
                return true;

            case R.id.popular_movies:
                getMovies(this.getResources().getString(R.string.popular));
                return true;

            case R.id.my_favorites:
                getMovies(this.getResources().getString(R.string.favorite));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Purpose : Setup Asynctask call with appropriate parameter
     *
     * @param call : The option selected from Menu
     */
    private void getMovies(String call) {

        switch (call) {

            case "favorite":
                new MovieTask(this, this).execute(getString(R.string.favorite));
                break;

            case "popular":
                new MovieTask(this, this).execute(getString(R.string.popular));
                break;

            case "top_rated":
                new MovieTask(this, this).execute(getString(R.string.top_rated));
                break;

            default:
                break;
        }
    }

    /**
     * Purpose to call TMDB for fresh data
     *
     * @param v : Update button at bottom of screen
     */
    public void update(View v) {

        getMovies(getString(R.string.popular));
    }

    /**
     * Purpose : Launch DetailActivity with appropriate movie information
     *
     * @param itemId : Position of movie clicked in arraylist
     */
    @Override
    public void onItemClickListener(int itemId, ImageView imageview) {

        final String MOVIE = getResources().getString(R.string.movieKey);
        Film movie = mFilms.get(itemId);

        if (movie != null) {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(MOVIE, movie);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imageview, ViewCompat.getTransitionName(imageview));
            startActivity(i, options.toBundle());
        }
    }


    /**
     * Purpose : Updates the adapter with the appropriate movie list
     */
    public void updateAdapter() {

        mAdapter = new MovieAdapterRV(this, this, mFilms);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void displayAppropriateView() {
        if (null == mFilms || mFilms.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyTV.setVisibility(View.VISIBLE);
        } else {

            mRecyclerView.setVisibility(View.VISIBLE);
            emptyTV.setVisibility(View.GONE);
        }
    }

    /**
     * Purpose : to set mFilms to the arraylist return from network or db call
     *
     * @param movies : ArrayList of movies returned from {@link MovieTask#doInBackground(String... args)}
     */
    @Override
    public void onPostExecute(ArrayList<Film> movies) {

        this.mFilms = movies;
        displayAppropriateView();
        updateAdapter();
    }
}
