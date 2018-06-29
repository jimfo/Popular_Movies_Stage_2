package com.jimfo.popular_movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.model.Film;

public class DetailViewModel extends ViewModel {

    private static final String TAG = DetailViewModel.class.getSimpleName();

    private LiveData<Film> mFilm;

    DetailViewModel(AppDatabase db, String id) {
        mFilm = db.movieDao().loadMovieById(id);
    }

    public LiveData<Film> getMovie() {
        return mFilm;
    }
}
