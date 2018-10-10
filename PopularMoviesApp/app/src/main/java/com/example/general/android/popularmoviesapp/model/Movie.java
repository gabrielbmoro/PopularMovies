package com.example.general.android.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {

    private int voteCount;
    private long id;
    private int voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String backdropPath;
    public static final String VOTE_COUNT_KEY = "vote_count";
    public static final String ID_KEY = "id";
    public static final String VOTE_AVERAGE_KEY = "vote_average";
    public static final String TITLE_KEY = "title";
    public static final String POPULARITY_KEY = "popularity";
    public static final String POSTER_PATH = "poster_path";
    public static final String OVERVIEW_KEY = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String BACKDROP_PATH_KEY = "backdrop_path";

    public Movie() { }

    private Movie(Parcel in) {
        voteCount = in.readInt();
        id = in.readLong();
        voteAverage = in.readInt();
        popularity = in.readDouble();
        ArrayList<String> array = in.createStringArrayList();
        if(array.size() > 4) {
            title = array.get(0);
            posterPath = array.get(1);
            overview = array.get(2);
            releaseDate = array.get(3);
            backdropPath = array.get(4);
        }
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(voteCount);
        dest.writeLong(id);
        dest.writeInt(voteAverage);
        dest.writeDouble(popularity);
        ArrayList<String> lst = new ArrayList<>();
        lst.add(title);
        lst.add(posterPath);
        lst.add(overview);
        lst.add(releaseDate);
        lst.add(backdropPath);
        dest.writeStringList(lst);
    }
}
