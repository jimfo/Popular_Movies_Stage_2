package com.jimfo.popular_movies.data;

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
    List<Film> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovies(Film film);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovies(Film film);

    @Delete
    void deleteMovies(Film film);
}
