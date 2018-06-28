package com.jimfo.popular_movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.model.Film;

public class DetailViewModel extends ViewModel {

    private LiveData<Film> movie;

    public DetailViewModel(AppDatabase db, String id) {
        movie = db.movieDao().loadMovieById(id);
    }

    public LiveData<Film> getMovie() {
        return movie;
    }
}
