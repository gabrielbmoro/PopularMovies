package com.example.general.android.popularmoviesapp.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.general.android.popularmoviesapp.model.Movie;

import java.util.List;

/**
 * This interface is used to data manipulation using the movie table.
 */
@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE is_favorite == 1")
    List<Movie> loadAllFavoriteMovies();

    @Query("SELECT * FROM movie WHERE is_favorite == 1 and id == :id")
    List<Movie> getSomeFavoriteMovieAccordingId(long id);

    @Query("SELECT * FROM movie WHERE id == :id")
    LiveData<Movie> getLiveDataToFavoriteMovieAccordingId(long id);

    @Query("SELECT * FROM movie WHERE id == :id")
    Movie getMovieAccordingToId(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovie(Movie movie);

}
