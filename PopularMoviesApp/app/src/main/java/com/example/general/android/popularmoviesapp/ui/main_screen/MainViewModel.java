package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.database.AppDatabase;
import com.example.general.android.popularmoviesapp.ui.main_screen.tasks.MovieApiQueryTask;
import com.example.general.android.popularmoviesapp.ui.main_screen.tasks.UpdateRecyclerView;
import com.example.general.android.popularmoviesapp.util.AppExecutors;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> popularMovies = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> topRatedMovies = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> favoriteMovies = new MutableLiveData<>();
    private MovieItemAdapter movieItemAdapter;
    /**
     * Task responsable for require information to the api
     */
    private MovieApiQueryTask taskForPopularMovies;
    private MovieApiQueryTask taskForTopRatedMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);

        taskForPopularMovies = new MovieApiQueryTask(new UpdateRecyclerView() {
            @Override
            public void onUpdate(List<Movie> results) {
                popularMovies.setValue(results);
                movieItemAdapter.updateMovies(popularMovies.getValue());
            }
        }, MovieApiQueryTask.QueryKind.POPULAR_MOVIES, application.getString(R.string.api_key));
        taskForTopRatedMovies = new MovieApiQueryTask(new UpdateRecyclerView() {
            @Override
            public void onUpdate(List<Movie> results) {
                topRatedMovies.setValue(results);
                movieItemAdapter.updateMovies(topRatedMovies.getValue());
            }
        }, MovieApiQueryTask.QueryKind.TOP_RATED_MOVIES, application.getString(R.string.api_key));

    }

    void loadPopularMovies() {
        if (popularMovies.getValue() == null || (popularMovies.getValue() != null && popularMovies.getValue().isEmpty())) {
            if (NetworkUtils.hasInternetConnection(getApplication())) {
                taskForPopularMovies.execute();
            }
        } else {
            movieItemAdapter.updateMovies(popularMovies.getValue());
        }
    }

    void loadTopRatedMovies() {
        if (topRatedMovies.getValue() == null || (topRatedMovies.getValue() != null && topRatedMovies.getValue().isEmpty())) {
            if (NetworkUtils.hasInternetConnection(getApplication())) {
                taskForTopRatedMovies.execute();
            }
        } else {
            movieItemAdapter.updateMovies(topRatedMovies.getValue());
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

}
