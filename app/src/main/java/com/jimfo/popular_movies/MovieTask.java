package com.jimfo.popular_movies;

import android.os.AsyncTask;

import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.TmdbUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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

        return TmdbUtils.fetchMovieData(myRef.get(), args[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Film> result) {
        super.onPostExecute(result);

        mPostExecuteListener.onPostExecute(result);
    }

}
