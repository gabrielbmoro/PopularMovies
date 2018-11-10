package com.example.general.android.popularmoviesapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class connects with api service.
 * The methods are provided by udacity's android projects - From git hub (repository)
 */
public class NetworkUtils {

    /**
     * Base url
     */
    private static final String MOVIEAPI_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";

    public static URL buildURLToFetchTrailers(String a_strApiKey, Long movieId) {
        String urlFormatted = MOVIEAPI_URL + movieId.toString() + "/videos";
        Uri builtUri = Uri.parse(urlFormatted)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, a_strApiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * These method converts the URI in URL with api key autentication.
     *
     * @param a_strApiKey defined by API
     * @param movieId     used to find the review according the movie's id
     * @return URL formatted
     */
    public static URL buildURLToFetchReviews(String a_strApiKey, long movieId) {
        String urlFormatted = MOVIEAPI_URL + movieId + "/reviews";
        Uri builtUri = Uri.parse(urlFormatted)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, a_strApiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * These method converts the URI in URL with api key autentication.
     *
     * @param a_strApiKey defined by API
     * @param strSortedBy popular or top rated
     * @return URL formatted
     */
    public static URL buildURLToAccessMovies(String a_strApiKey, String strSortedBy) {
        String urlFormatted = MOVIEAPI_URL + strSortedBy;
        Uri builtUri = Uri.parse(urlFormatted)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, a_strApiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * These method make some request.
     *
     * @param url defines the request
     * @return some response in String
     * @throws IOException exception which may occur
     */
    public static String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Reference: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     *
     * @return true if there is connectivitity
     */
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}