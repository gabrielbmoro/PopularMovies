package com.example.general.android.popularmoviesapp.ui.main_screen.tasks;

import android.os.AsyncTask;

import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.util.JSONParser;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Task used to get information from api, and update the screen that implements
 * the UpdateRecyclerView contract.
 */
public class MovieApiQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

    private UpdateRecyclerView contract;
    private QueryKind query;
    private String apiKey;

    public MovieApiQueryTask(UpdateRecyclerView acontract, QueryKind queryKind, String apiKey) {
        this.contract = acontract;
        this.query = queryKind;
        this.apiKey = apiKey;
    }

    @Override
    protected ArrayList<Movie> doInBackground(URL... params) {

        URL searchURL = NetworkUtils.buildURLToAccessMovies(this.apiKey, getParameterAccordingQuery());
        ArrayList<Movie> results = null;
        try {
            results = JSONParser.gettingResultsValues(
                    NetworkUtils.getResponseFromHttpURL(searchURL)
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> results) {
        if (contract != null && results != null) {
            contract.onUpdate(results);
        }
    }

    private String getParameterAccordingQuery() {
        String parameter;
        if (query == QueryKind.POPULAR_MOVIES) {
            parameter = "popular";
        } else if (query == QueryKind.TOP_RATED_MOVIES) {
            parameter = "top_rated";
        } else {
            parameter = "popular";
        }
        return parameter;
    }

    public enum QueryKind {POPULAR_MOVIES, TOP_RATED_MOVIES}

    public interface UpdateRecyclerView {
        void onUpdate(List<Movie> results);
    }

}
