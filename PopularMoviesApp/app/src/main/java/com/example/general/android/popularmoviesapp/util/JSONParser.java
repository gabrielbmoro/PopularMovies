package com.example.general.android.popularmoviesapp.util;

import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.Review;
import com.example.general.android.popularmoviesapp.model.VideoTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class convert the json string in a movies list
 * Reference: https://stackoverflow.com/questions/16769809/json-object-how-to-get-values
 */
public class JSONParser {

    public static ArrayList<Movie> gettingResultsValues(String astrJsonText) {
        JSONObject results;
        ArrayList<Movie> moviesLst = new ArrayList<>();
        try {
            results = new JSONObject(astrJsonText);
            JSONArray jsonObjects = results.getJSONArray("results");
            JSONObject jsonObject;
            for (int count = 0; count < jsonObjects.length(); count++) {
                jsonObject = jsonObjects.getJSONObject(count);
                Movie movieToBeReturned = new Movie();
                movieToBeReturned.setId(jsonObject.getLong(Movie.ID_KEY));
                movieToBeReturned.setVoteAverage(jsonObject.getInt(Movie.VOTE_AVERAGE_KEY));
                movieToBeReturned.setTitle(jsonObject.getString(Movie.TITLE_KEY));
                movieToBeReturned.setPosterPath(jsonObject.getString(Movie.POSTER_PATH));
                movieToBeReturned.setOverview(jsonObject.getString(Movie.OVERVIEW_KEY));
                movieToBeReturned.setReleaseDate(jsonObject.getString(Movie.RELEASE_DATE));
                moviesLst.add(movieToBeReturned);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesLst;
    }

    public static ArrayList<VideoTrailer> gettingVideoResultsValues(String astrJsonText) {
        JSONObject results;
        ArrayList<VideoTrailer> videosLst = new ArrayList<>();
        if (astrJsonText != null) {
            try {
                results = new JSONObject(astrJsonText);
                JSONArray jsonObjects = results.getJSONArray("results");
                JSONObject jsonObject;
                for (int count = 0; count < jsonObjects.length(); count++) {
                    jsonObject = jsonObjects.getJSONObject(count);
                    VideoTrailer videoToBeReturned = new VideoTrailer();
                    videoToBeReturned.setId(jsonObject.getString(VideoTrailer.ID_KEY));
                    videoToBeReturned.setKey(jsonObject.getString(VideoTrailer.KEY_KEY));
                    videoToBeReturned.setName(jsonObject.getString(VideoTrailer.NAME_KEY));
                    videoToBeReturned.setSize(jsonObject.getInt(VideoTrailer.SIZE_KEY));
                    videoToBeReturned.setType(jsonObject.getString(VideoTrailer.TYPE_KEY));
                    videosLst.add(videoToBeReturned);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return videosLst;
    }

    public static ArrayList<Review> gettingReviewResultsValues(String astrJsonText) {
        JSONObject results;
        ArrayList<Review> reviewLst = new ArrayList<>();
        if (astrJsonText != null) {
            try {
                results = new JSONObject(astrJsonText);
                JSONArray jsonObjects = results.getJSONArray("results");
                JSONObject jsonObject;
                for (int count = 0; count < jsonObjects.length(); count++) {
                    jsonObject = jsonObjects.getJSONObject(count);
                    Review reviewToBeReturned = new Review();
                    reviewToBeReturned.setAuthor(jsonObject.getString(Review.AUTHOR_KEY));
                    reviewToBeReturned.setContent(jsonObject.getString(Review.CONTENT_KEY));
                    reviewToBeReturned.setId(jsonObject.getString(Review.ID_KEY));
                    reviewLst.add(reviewToBeReturned);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviewLst;
    }
}
