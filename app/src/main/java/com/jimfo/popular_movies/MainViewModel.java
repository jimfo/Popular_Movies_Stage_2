package com.jimfo.popular_movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.model.Film;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Film>> films;

    public MainViewModel(Application application) {
        super(application);

        AppDatabase mDb = AppDatabase.getsInstance(application);
        films = mDb.movieDao().loadAllMovies();
    }

    public LiveData<List<Film>> getMovies() {
        return films;
    }
}
