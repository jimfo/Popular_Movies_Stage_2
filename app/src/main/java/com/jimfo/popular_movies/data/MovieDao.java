package com.jimfo.popular_movies.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jimfo.popular_movies.model.Film;

import java.util.List;

public interface MovieDao {

    @Query("SELECT * FROM movies")
    List<Film> loadAllTasks();

    @Insert
    void insertTask(Film film);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Film film);

    @Delete
    void deleteTask(Film film);
}
