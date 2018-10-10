package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.os.AsyncTask;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.util.JSONParser;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Task used to get information from api, and update the screen that implements
 * the UpdateRecyclerView contract.
 */
class MovieApiQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

    private UpdateRecyclerView contract;

    MovieApiQueryTask(UpdateRecyclerView acontract){
        contract = acontract;
    }

    @Override
    protected ArrayList<Movie> doInBackground(URL... params) {
        URL searchURL = params[0];
        ArrayList<Movie> results = null;
        try {
            results = JSONParser.gettingResultsValues(
                    NetworkUtils.getResponseFromHttpURL(searchURL)
            );

        } catch (IOException e){
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> results) {
        if(contract != null && results != null) {
            contract.onUpdate(results);
        }
    }

    interface UpdateRecyclerView {
        void onUpdate(ArrayList<Movie> results);
    }
}
