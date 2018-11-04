package com.example.general.android.popularmoviesapp.ui.details.reviews;

import android.os.AsyncTask;

import com.example.general.android.popularmoviesapp.model.Review;
import com.example.general.android.popularmoviesapp.util.JSONParser;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReviewsApiQueryTask extends AsyncTask<URL, Void, ArrayList<Review>> {

    private UpdateRecyclerView contract;
    private String apiKey;
    private long nMovieId;

    public ReviewsApiQueryTask(String apiKey, long movieId, UpdateRecyclerView acontract) {
        this.apiKey = apiKey;
        this.contract = acontract;
        this.nMovieId = movieId;
    }

    @Override
    protected ArrayList<Review> doInBackground(URL... params) {
        URL searchURL = NetworkUtils.buildURLToFetchReviews(apiKey, nMovieId);
        String response = null;
        ArrayList<Review> reviewLst = null;

        try {
            response = NetworkUtils.getResponseFromHttpURL(searchURL);
            reviewLst = JSONParser.gettingReviewResultsValues(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviewLst;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> lst) {
        super.onPostExecute(lst);
        if (contract != null && lst != null) {
            contract.onUpdate(lst);
        }
    }

    public interface UpdateRecyclerView {
        void onUpdate(ArrayList<Review> results);
    }
}