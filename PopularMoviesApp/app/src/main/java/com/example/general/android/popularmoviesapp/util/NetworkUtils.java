package com.example.general.android.popularmoviesapp.util;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class connects with api service.
 */
public class NetworkUtils {

    /**
     * Base url
     */
    private static final String MOVIEAPI_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM = "api_key";

    public static URL buildURLToAccessMovies(String a_strApiKey, String strSortedBy) {
        String urlFormatted = MOVIEAPI_URL + strSortedBy;
        Uri builtUri = Uri.parse(urlFormatted)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, a_strApiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        Log.d("TESTT", urlConnection.toString());

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if(scanner.hasNext()){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}