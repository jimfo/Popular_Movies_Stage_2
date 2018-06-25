package com.jimfo.popular_movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.jimfo.popular_movies.adapter.MovieAdapterRV;
import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterRV.ItemClickListener,
        MovieTask.PostExecuteListener {

    // The code for ItemClickListener came from https://piercezaifman.com/click-listener-for-recyclerview-adapter/
    // by Pierce Zaifman

    private static final String TAG = MainActivity.class.getSimpleName();

    private AppDatabase mDb;
    private String ab_title;
    public MovieAdapterRV mAdapter;
    public RecyclerView mRecyclerView;
    private List<Film> mFilms;
    private String lastcall = "popular";
    private static int currentVisiblePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            lastcall = savedInstanceState.getString(getResources().getString(R.string.movieKey));
        }

        mRecyclerView = findViewById(R.id.rv_movies);
        LinearLayout refreshLL = findViewById(R.id.refreshbar_ll);
        refreshLL.setBackgroundColor(getResources().getColor(R.color.ll_color));
        mDb = AppDatabase.getsInstance(getApplicationContext());
        ab_title = getResources().getString(R.string.popular_movies);

        // Layout determined by orientation
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        if (NetworkUtils.isNetworkAvailable(this.getApplicationContext())) {

            // if db is empty check if network is available and get movies from TMDB
            new MovieTask(this, this).execute(getResources().getString(R.string.popular));
            setTitle(getResources().getString(R.string.popular_movies));
            lastcall = getResources().getString(R.string.popular);
        }
        else{
            setupViewModel();
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        currentVisiblePosition = 0;
        currentVisiblePosition = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    }

    @Override
    public void onResume(){
        super.onResume();

        getMovies(lastcall);
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

        switch (item.getItemId()) {
            case R.id.top_rated:
                ab_title = this.getResources().getString(R.string.top_rated_movies);
                lastcall = getResources().getString(R.string.top_rated);
                getMovies(this.getResources().getString(R.string.top_rated));
                return true;

            case R.id.popular_movies:
                ab_title = this.getResources().getString(R.string.popular_movies);
                lastcall = getResources().getString(R.string.popular);
                getMovies(this.getResources().getString(R.string.popular));
                return true;

            case R.id.my_favorites:
                setTitle(this.getResources().getString(R.string.favorite_movies));
                lastcall = getResources().getString(R.string.favorite);
                getMovies(this.getResources().getString(R.string.favorite));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(getResources().getString(R.string.movieKey), lastcall);
    }


    /**
     * Purpose : Setup Asynctask call with appropriate parameter
     *
     * @param call : The option selected from Menu
     */
    private void getMovies(String call) {

        switch (call) {
            case "favorite":
                ab_title = getResources().getString(R.string.favorite_movies);
                setupViewModel();
                break;

            case "popular":
                ab_title = getResources().getString(R.string.popular_movies);
                new MovieTask(this, this).execute(getString(R.string.popular));
                break;

            case "top_rated":
                ab_title = getResources().getString(R.string.top_rated_movies);
                new MovieTask(this, this).execute(getString(R.string.top_rated));
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
    public void onItemClickListener(int itemId) {

        final String MOVIE = "movie";

        Film movie = mFilms.get(itemId);

        if (movie != null) {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(MOVIE, movie);
            startActivity(i);
        }

    }


    /**
     * Purpose : Updates the adapter with the appropriate movie list
     */
    public void updateAdapter() {
        mAdapter = new MovieAdapterRV(this, this, mFilms);
        mRecyclerView.setAdapter(mAdapter);
        setTitle(ab_title);

        (mRecyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        currentVisiblePosition = 0;
    }

    private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(@Nullable List<Film> films) {
                if(null == films){
                    mFilms = new ArrayList<>();
                }
                else{
                    mFilms = films;
                }
                mAdapter.setMovies(mFilms);
            }
        });
    }

    /**
     * Purpose : to set mFilms to the arraylist return from network or db call
     *
     * @param movies : ArrayList of movies returned from {@link MovieTask#doInBackground(String... args)}
     */
    @Override
    public void onPostExecute(ArrayList<Film> movies) {

        this.mFilms = movies;
        updateAdapter();
    }
}
