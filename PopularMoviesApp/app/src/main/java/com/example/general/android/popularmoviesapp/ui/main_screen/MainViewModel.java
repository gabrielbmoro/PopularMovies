package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.general.android.popularmoviesapp.model.Movie;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Movie>> popularMovies = new MutableLiveData();
    private MutableLiveData<ArrayList<Movie>> topRatedMovies = new MutableLiveData();


    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    MutableLiveData<ArrayList<Movie>> getPopularMovies() {
        return popularMovies;
    }

    MutableLiveData<ArrayList<Movie>> getTopRatedMovies() {
        return topRatedMovies;
    }

    void setPopularMovies(ArrayList<Movie> movies) {
        popularMovies.setValue(movies);
    }

    void setTopRatedMovies(ArrayList<Movie> movies) {
        topRatedMovies.setValue(movies);
    }

    public void clearMovieLists() {
        if(popularMovies.getValue() != null) popularMovies.getValue().clear();
        if(topRatedMovies.getValue() != null) topRatedMovies.getValue().clear();
    }

}
