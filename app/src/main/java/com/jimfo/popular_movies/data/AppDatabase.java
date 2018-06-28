package com.jimfo.popular_movies.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.jimfo.popular_movies.model.Film;

@Database(entities = {Film.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static String DATABASE_NAME = "movies_db";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.i(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME
                )
                        .build();
            }
        }
        Log.d(TAG, "Getting database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
