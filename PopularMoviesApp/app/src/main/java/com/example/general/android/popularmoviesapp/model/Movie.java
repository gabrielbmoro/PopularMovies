package com.example.general.android.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Parcelable implementation was based in:
 *
 * @link https://stackoverflow.com/questions/36071023/how-to-make-class-with-multiple-integers-and-strings-parcelable
 */
public class Movie implements Parcelable {

    /**
     * Movie properties
     */
    private long id;
    private int voteAverage;
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    /**
     * These variables represent all keys used in json mapping for each movie property.
     */
    public static final String ID_KEY = "id";
    public static final String VOTE_AVERAGE_KEY = "vote_average";
    public static final String TITLE_KEY = "title";
    public static final String POSTER_PATH = "poster_path";
    public static final String OVERVIEW_KEY = "overview";
    public static final String RELEASE_DATE = "release_date";

    public Movie() {
        id = 0;
        voteAverage = 0;
        title = "";
        posterPath = "";
        overview = "";
        releaseDate = "";
    }

    /**
     * Constructor method used when the object is converted from parcel to object format.
     *
     * @param in defines the parcel object.
     */
    private Movie(Parcel in) {
        id = in.readLong();
        voteAverage = in.readInt();
        ArrayList<String> array = in.createStringArrayList();
        if (array != null && array.size() > 3) {
            title = array.get(0);
            posterPath = array.get(1);
            overview = array.get(2);
            releaseDate = array.get(3);
        }
    }

    /**
     * This method is used by parcel implementation.
     */
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        /**
         * Call the constructor to convert parcel to object.
         * @param in defines some parcel object
         * @return a movie object
         */
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        /**
         * I don't why this method is used.
         */
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    /**
     * I don't why this method is used.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * The parcel object is created using the movie properties
     *
     * @param dest  parcel object that will be generated
     * @param flags don't know why this parameter is
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(voteAverage);
        ArrayList<String> lst = new ArrayList<>();
        lst.add(title);
        lst.add(posterPath);
        lst.add(overview);
        lst.add(releaseDate);
        dest.writeStringList(lst);
    }
}
