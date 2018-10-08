package com.example.general.android.popularmoviesapp.util;

import com.example.general.android.popularmoviesapp.model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class JSONParser {

    public static ArrayList<Movie> gettingResultsValues(String astrJsonText) {
        JSONObject results = null;
        ArrayList<Movie> moviesLst = new ArrayList<>();
        try {
            results = new JSONObject(astrJsonText);
            JSONArray jsonObjects = results.getJSONArray("results");
            JSONObject jsonObject = null;
            for (int count = 0; count < jsonObjects.length(); count++) {
                jsonObject = jsonObjects.getJSONObject(count);
                Movie movieToBeReturned = new Movie();
                movieToBeReturned.setVoteCount(jsonObject.getInt(Movie.VOTE_COUNT_KEY));
                movieToBeReturned.setId(jsonObject.getLong(Movie.ID_KEY));
                movieToBeReturned.setHasVideo(jsonObject.getBoolean(Movie.VIDEO_KEY));
                movieToBeReturned.setVoteAverage(jsonObject.getInt(Movie.VOTE_AVERAGE_KEY));
                movieToBeReturned.setTitle(jsonObject.getString(Movie.TITLE_KEY));
                movieToBeReturned.setPopularity(jsonObject.getDouble(Movie.POPULARITY_KEY));
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
}
