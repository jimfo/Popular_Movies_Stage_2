package com.jimfo.popular_movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.model.Film;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private LiveData<List<Film>> mFilms;
    private LiveData<Film> mFilm;

    public MainViewModel(Application application) {
        super(application);

        AppDatabase mDb = AppDatabase.getsInstance(application);
        mFilms = mDb.movieDao().loadAllMovies();
    }

    public LiveData<Film> getMovie() {return mFilm;}

    public LiveData<List<Film>> getMovies() {
        return mFilms;
    }
}
