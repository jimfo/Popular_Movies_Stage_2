package com.jimfo.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jimfo.popular_movies.adapter.MovieAdapterRV;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.Colors;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickListener, MovieTask.PostExecuteListener {

    // The code for ItemClickListener came from https://piercezaifman.com/click-listener-for-recyclerview-adapter/
    // by Pierce Zaifman

    private static final String TAG = MainActivity.class.getSimpleName();

    private String selection = "";
    public MovieAdapterRV mAdapter;
    public RecyclerView mRecyclerView;
    private ArrayList<Film> mFilms;
    private LinearLayout refreshLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        refreshLL = findViewById(R.id.refreshbar_ll);
        refreshLL.setBackgroundColor(getResources().getColor(R.color.ll_color));

        // Layout determined by orientation
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        Bundle mExtras;
        Intent i = getIntent();
        mExtras = i.getExtras();

        if (mExtras != null) {
            if (mExtras.containsKey(this.getResources().getString(R.string.select))) {
                selection = mExtras.getString(this.getResources().getString(R.string.select));
            }
        }
        else {
            selection = this.getResources().getString(R.string.popular);
        }

        if (isNetworkAvailable()) {
            // Default task will be Popular Movies
            mFilms = new ArrayList<>();
            getMovies();

            if (selection.equals(this.getResources().getString(R.string.top_rated)))
                setTitle(this.getResources().getString(R.string.top_rated_movies));
            else
                setTitle(this.getResources().getString(R.string.popular_movies));
        }
        else {
            closeOnError();
        }
    }

    /**
     * Purpose : Method to check for network connectivity
     *
     * @return : boolean
     */
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;

        if (connectivityMgr != null) {
            networkInfo = connectivityMgr.getActiveNetworkInfo();

            // if no network is available networkInfo will be null
            return (networkInfo != null && networkInfo.isConnected());
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.top_rated:
                setTitle(this.getResources().getString(R.string.top_rated_movies));
                selection = this.getResources().getString(R.string.top_rated);
                getMovies();
                return true;
            case R.id.popular_movies:
                setTitle(this.getResources().getString(R.string.popular_movies));
                selection = this.getResources().getString(R.string.popular);
                getMovies();
                return true;
            case R.id.my_favorites:
                setTitle(this.getResources().getString(R.string.favorite_movies));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getMovies() {

        new MovieTask(this, this).execute(selection);


    }

    @Override
    public void onClick(View view, int position) {
        Film movie = mFilms.get(position);
        if (movie != null) {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("movie", movie);
            i.putExtra("select", selection);
            startActivity(i);
        }
        else {
            closeOnError();
        }
    }

    // Handle null object
    public void closeOnError() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPostExecute(ArrayList<Film> movies) {

        this.mFilms = movies;

        mAdapter = new MovieAdapterRV(this, movies);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setClickListener(this);
    }
}
