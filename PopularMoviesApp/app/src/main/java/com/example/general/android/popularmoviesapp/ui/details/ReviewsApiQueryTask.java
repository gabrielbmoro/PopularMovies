package com.example.general.android.popularmoviesapp.ui.details;

import android.os.AsyncTask;

import com.example.general.android.popularmoviesapp.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class ReviewsApiQueryTask extends AsyncTask<URL, Void, String> {

    private UpdateRecyclerView contract;

    ReviewsApiQueryTask(UpdateRecyclerView acontract) {
        contract = acontract;
    }

    @Override
    protected String doInBackground(URL... params) {
        URL searchURL = params[0];
        String response = null;

        try {
            response = NetworkUtils.getResponseFromHttpURL(searchURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (contract != null && s != null) {
            contract.onUpdate(s);
        }
    }

    interface UpdateRecyclerView {
        void onUpdate(String results);
    }
}