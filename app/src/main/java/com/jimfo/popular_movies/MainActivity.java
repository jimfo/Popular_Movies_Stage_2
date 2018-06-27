package com.jimfo.popular_movies;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jimfo.popular_movies.adapter.MovieAdapterRV;
import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterRV.ItemClickListener,
        MovieTask.PostExecuteListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String SAVED_STATE = "state";

    private String ab_title;
    public MovieAdapterRV mAdapter;
    public RecyclerView mRecyclerView;
    private List<Film> mFilms;
    private TextView emptyTV;
    Parcelable recyclerviewState;
    RecyclerView.LayoutManager mLayoutManager;


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
        }

        mRecyclerView = findViewById(R.id.rv_movies);
        LinearLayout refreshLL = findViewById(R.id.refreshbar_ll);
        refreshLL.setBackgroundColor(getResources().getColor(R.color.ll_color));
        ab_title = getResources().getString(R.string.popular_movies);
        emptyTV = findViewById(R.id.empty_view);

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
        }
        else{
            setupViewModel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        recyclerviewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        state.putParcelable(SAVED_STATE, recyclerviewState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        if(state != null)
            mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (recyclerviewState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerviewState);
        }
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
                getMovies(this.getResources().getString(R.string.top_rated));
                return true;

            case R.id.popular_movies:
                ab_title = this.getResources().getString(R.string.popular_movies);
                getMovies(this.getResources().getString(R.string.popular));
                return true;

            case R.id.my_favorites:
                setTitle(this.getResources().getString(R.string.favorite_movies));
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
    public void onItemClickListener(int itemId, ImageView imageview) {

        final String MOVIE = getResources().getString(R.string.movieKey);
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

     //   (mRecyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
     //   currentVisiblePosition = 0;
    }

    private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(@Nullable List<Film> films) {

                if(null == films || films.size() == 0){
                    mFilms = new ArrayList<>();
                }
                else{
                    mFilms = films;
                }

                displayAppropriateView();
                mAdapter.setMovies(mFilms);
            }
        });
    }

    public void displayAppropriateView(){
        if(null == mFilms || mFilms.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyTV.setVisibility(View.VISIBLE);
        }
        else{

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
