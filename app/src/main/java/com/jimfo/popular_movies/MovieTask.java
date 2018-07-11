package com.jimfo.popular_movies;

import android.os.AsyncTask;

import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.NetworkUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MovieTask extends AsyncTask<String, Void, ArrayList<Film>> {

    private final WeakReference<MainActivity> myRef;

    private PostExecuteListener mPostExecuteListener;

    public interface PostExecuteListener {
        void onPostExecute(ArrayList<Film> movies);
    }

    MovieTask(MainActivity activity, PostExecuteListener pel) {

        myRef = new WeakReference<>(activity);
        this.mPostExecuteListener = pel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Film> doInBackground(String... args) {

        ArrayList<Film> films = new ArrayList<>();

        switch (args[0]){
            case "popular":
            case "top_rated":
                films = NetworkUtils.fetchMovieData(myRef.get(), args[0]);
                break;

            case "favorite":
                films = new ArrayList<>(AppDatabase.getsInstance(myRef.get()).movieDao().loadFavoritedMovies());
                break;

            default:
                break;
        }
        return films;
    }

    @Override
    protected void onPostExecute(ArrayList<Film> result) {
        super.onPostExecute(result);

        mPostExecuteListener.onPostExecute(result);
    }
}
