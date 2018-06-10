package com.jimfo.popular_movies.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jimfo.popular_movies.BuildConfig;
import com.jimfo.popular_movies.R;
import com.jimfo.popular_movies.model.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TmdbUtils {

    private static final String TAG = TmdbUtils.class.getSimpleName();

    // https://stackoverflow.com/questions/14570989/best-practice-for-storing-and-protecting-private-api-keys-in-applications
    // Answered by SANAT
    private static final String KEY = BuildConfig.TmdbSecAPIKey;


    private TmdbUtils() {
    }

    public static ArrayList<Film> extractMovieData(Context context, String response, String request) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        Film film;
        String id;
        String title;
        String releaseDate;
        String rating;
        String overview;
        String posterPath;
        String backdropPath;
        String language;

        ArrayList<Film> films = new ArrayList<>();

        // Try to parse the JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Parse the response given by the JSON_RESPONSE string and
            // build up a list of Film objects with the corresponding data.
            JSONObject jsonObj = new JSONObject(response);

            if (!jsonObj.has(context.getString(R.string.statusCode))) {
                if (jsonObj.has(context.getString(R.string.results))) {

                    JSONArray movieResults = jsonObj.optJSONArray(context.getString(R.string.results));

                    if (movieResults != null) {
                        for (int i = 0; i < movieResults.length(); i++) {

                            JSONObject jo = movieResults.getJSONObject(i);

                            id           = jo.optString(context.getString(R.string.id), context.getString(R.string.dna));
                            title        = jo.optString(context.getString(R.string.title), context.getString(R.string.dna));
                            releaseDate  = jo.optString(context.getString(R.string.releaseDate), context.getString(R.string.dna));
                            rating       = jo.optString(context.getString(R.string.voteAverage), context.getString(R.string.dna));
                            overview     = jo.optString(context.getString(R.string.overview), context.getString(R.string.dna));
                            posterPath   = context.getString(R.string.path) + jo.optString(context.getString(R.string.posterPath), context.getString(R.string.dna));
                            backdropPath = context.getString(R.string.path) + jo.optString(context.getString(R.string.backDrop), context.getString(R.string.dna));
                            language     = jo.optString(context.getString(R.string.language), context.getString(R.string.dna));

                            film = new Film(id,title,releaseDate,rating,overview,posterPath,backdropPath,language,request);
                            films.add(film);
                        }
                    }
                }
            }
            else {
                Toast.makeText(context, context.getString(R.string.noResource), Toast.LENGTH_LONG).show();
                return null;
            }
        }
        catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(TAG, context.getString(R.string.parseError), e);
        }
        return films;
    }
}
