package com.example.general.android.popularmoviesapp.util;

import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.VideoTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class convert the json string in a movies list
 */
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
        JSONObject results = null;
        ArrayList<VideoTrailer> videosLst = new ArrayList<>();
        if (astrJsonText != null) {
            try {
                results = new JSONObject(astrJsonText);
                JSONArray jsonObjects = results.getJSONArray("results");
                JSONObject jsonObject = null;
                for (int count = 0; count < jsonObjects.length(); count++) {
                    jsonObject = jsonObjects.getJSONObject(count);
                    VideoTrailer videoToBeReturned = new VideoTrailer();
                    videoToBeReturned.setId(jsonObject.getString(VideoTrailer.ID_KEY));
                    videoToBeReturned.setIso6391Format(jsonObject.getString(VideoTrailer.ISO_6391_FORMAT_KEY));
                    videoToBeReturned.setIso31661Format(jsonObject.getString(VideoTrailer.ISO_3166_FORMAT_KEY));
                    videoToBeReturned.setKey(jsonObject.getString(VideoTrailer.KEY_KEY));
                    videoToBeReturned.setName(jsonObject.getString(VideoTrailer.NAME_KEY));
                    videoToBeReturned.setSite(jsonObject.getString(VideoTrailer.SITE_KEY));
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
}
