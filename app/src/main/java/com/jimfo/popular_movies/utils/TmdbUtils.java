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

    public static ArrayList<Film> fetchMovieData(Context context, String request) {
        // Create URL object
        URL requestURL = createUrl(context, context.getString(R.string.baseUrl) + request + "?api_key=" + KEY);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(context, requestURL);
        }
        catch (IOException e) {
            Log.e(TAG, context.getString(R.string.inputstreamError), e);
        }

        return extractMovieData(context, jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(Context context, String stringUrl) {

        URL url;
        try {
            url = new URL(stringUrl);
        }
        catch (MalformedURLException exception) {
            Log.e(TAG, context.getString(R.string.urlError) + exception);
            return null;
        }

        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(Context context, URL url) throws IOException {

        String jsonResponse = "";

        // Check if url is null
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(context.getString(R.string.get));
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(context, inputStream);
            }
            else {
                Log.v(TAG, context.getString(R.string.responseCodeError) + urlConnection.getResponseCode());
            }
        }
        catch (IOException e) {
            Log.e(TAG, context.getString(R.string.jsonError) + e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // TODO function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(Context context, InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(context.getString(R.string.utf8)));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    private static ArrayList<Film> extractMovieData(Context context, String response) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        Film film;
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
                            film = new Film();

                            film.setmId(jo.optString(context.getString(R.string.id), context.getString(R.string.dna)));
                            film.setmVoteAverage(jo.optString(context.getString(R.string.voteAverage), context.getString(R.string.dna)));
                            film.setmTitle(jo.optString(context.getString(R.string.title), context.getString(R.string.dna)));
                            film.setmMoviePoster(context.getString(R.string.path) + jo.optString(context.getString(R.string.posterPath), context.getString(R.string.dna)));
                            film.setmPlotSummary(jo.optString(context.getString(R.string.overview), context.getString(R.string.dna)));
                            film.setmReleaseDate(jo.optString(context.getString(R.string.releaseDate), context.getString(R.string.dna)));
                            film.setmBackdrop(context.getString(R.string.path) + jo.optString(context.getString(R.string.backDrop), context.getString(R.string.dna)));
                            film.setmLanguage(jo.optString(context.getString(R.string.language), context.getString(R.string.dna)));

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
