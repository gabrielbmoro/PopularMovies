package com.example.general.android.popularmoviesapp.ui.details;

import android.os.AsyncTask;

import com.example.general.android.popularmoviesapp.model.VideoTrailer;
import com.example.general.android.popularmoviesapp.util.JSONParser;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class TrailerApiQueryTask extends AsyncTask<URL, Void, ArrayList<VideoTrailer>> {

    private UpdateRecyclerView contract;

    TrailerApiQueryTask(UpdateRecyclerView acontract) {
        contract = acontract;
    }

    @Override
    protected ArrayList<VideoTrailer> doInBackground(URL... params) {
        URL searchURL = params[0];

        ArrayList<VideoTrailer> results = null;
        try {

            String response = NetworkUtils.getResponseFromHttpURL(searchURL);
            results = JSONParser.gettingVideoResultsValues(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<VideoTrailer> results) {
        super.onPostExecute(results);
        if (contract != null && results != null) {
            contract.onUpdate(results);
        }
    }

    interface UpdateRecyclerView {
        void onUpdate(ArrayList<VideoTrailer> results);
    }
}
