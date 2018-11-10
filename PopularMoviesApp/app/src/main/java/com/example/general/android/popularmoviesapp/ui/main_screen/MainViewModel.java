package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.database.AppDatabase;
import com.example.general.android.popularmoviesapp.ui.main_screen.tasks.MovieApiQueryTask;
import com.example.general.android.popularmoviesapp.util.AppExecutors;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> popularMovies = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> topRatedMovies = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> favoriteMovies = new MutableLiveData<>();
    private MovieItemAdapter movieItemAdapter;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    void loadPopularMovies(final AfterLoading afterLoading) {
        afterLoading.showRefresh();
        if (popularMovies.getValue() == null || (popularMovies.getValue() != null && popularMovies.getValue().isEmpty())) {
            if (NetworkUtils.hasInternetConnection(getApplication())) {
                afterLoading.showRefresh();
                new MovieApiQueryTask(new MovieApiQueryTask.UpdateRecyclerView() {
                    @Override
                    public void onUpdate(List<Movie> results) {
                        popularMovies.setValue(results);
                        movieItemAdapter.updateMovies(popularMovies.getValue());
                        afterLoading.hideRefresh();
                    }
                }, MovieApiQueryTask.QueryKind.POPULAR_MOVIES, getApplication().getString(R.string.api_key)).execute();
            }
        } else {
            movieItemAdapter.updateMovies(popularMovies.getValue());
            afterLoading.hideRefresh();
        }
    }

    void loadTopRatedMovies(final AfterLoading afterLoading) {
        afterLoading.showRefresh();
        if (topRatedMovies.getValue() == null || (topRatedMovies.getValue() != null && topRatedMovies.getValue().isEmpty())) {
            if (NetworkUtils.hasInternetConnection(getApplication())) {
                new MovieApiQueryTask(new MovieApiQueryTask.UpdateRecyclerView() {
                    @Override
                    public void onUpdate(List<Movie> results) {
                        topRatedMovies.setValue(results);
                        movieItemAdapter.updateMovies(topRatedMovies.getValue());
                        afterLoading.hideRefresh();
                    }
                }, MovieApiQueryTask.QueryKind.TOP_RATED_MOVIES, getApplication().getString(R.string.api_key)).execute();
            }
        } else {
            movieItemAdapter.updateMovies(topRatedMovies.getValue());
            afterLoading.hideRefresh();
        }
    }

    void loadFavoriteMovies() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Movie> lstResult = AppDatabase.getInstance(getApplication())
                        .movieDao()
                        .loadAllFavoriteMovies();
                AppExecutors.getInstance().getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        favoriteMovies.setValue(lstResult);
                        movieItemAdapter.updateMovies(favoriteMovies.getValue());
                    }
                });
            }
        });
    }

    void setAdapter(@NonNull MovieItemAdapter adapterX) {
        this.movieItemAdapter = adapterX;
    }

    public interface AfterLoading {
        void showRefresh();

        void hideRefresh();
    }

}
