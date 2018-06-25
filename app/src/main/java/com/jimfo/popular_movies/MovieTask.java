package com.jimfo.popular_movies;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.data.MovieDao;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.NetworkUtils;
import com.jimfo.popular_movies.utils.TmdbUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieTask extends AsyncTask<String, Void, ArrayList<Film>> {

    private static final String TAG = MovieTask.class.getSimpleName();

    private final WeakReference<MainActivity> myRef;

    private PostExecuteListener mPostExecuteListener;

    public interface PostExecuteListener{
        void onPostExecute(ArrayList<Film> movies);
    }

    MovieTask(MainActivity activity, PostExecuteListener pel){

        myRef = new WeakReference<>(activity);
        this.mPostExecuteListener = pel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Film> doInBackground(String... args) {

        return NetworkUtils.fetchMovieData(myRef.get(), args[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Film> result) {
        super.onPostExecute(result);

        mPostExecuteListener.onPostExecute(result);
    }
}
