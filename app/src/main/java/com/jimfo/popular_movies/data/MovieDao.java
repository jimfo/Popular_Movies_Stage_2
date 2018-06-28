package com.jimfo.popular_movies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jimfo.popular_movies.model.Film;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Film>> loadAllMovies();

    @Query("SELECT * FROM movies")
    List<Film> loadFavoritedMovies();

    @Query("SELECT count(*) FROM movies WHERE movie_id = :id")
    int checkIfMovieInTable(String id);

    @Query("SELECT * FROM movies WHERE movie_id = :id")
    LiveData<Film> loadMovieById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(Film film);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Film film);

    @Delete
    int deleteMovie(Film film);
}
