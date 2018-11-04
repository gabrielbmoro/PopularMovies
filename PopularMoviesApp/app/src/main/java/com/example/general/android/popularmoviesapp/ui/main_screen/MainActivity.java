package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.databinding.ActivityMainDiscoveryScreenBinding;
import com.example.general.android.popularmoviesapp.model.Movie;

import java.util.ArrayList;

/**
 * Main screen
 */
public class MainActivity extends AppCompatActivity implements MovieApiQueryTask.UpdateRecyclerView {

    protected RecyclerView rcRecyclerView;
    protected Spinner spCriteria;
    private MainViewModel viewModel;
    /**
     * Task responsable for require information to the api
     */
    private MovieApiQueryTask taskForPopularMovies;
    private MovieApiQueryTask taskForTopRatedMovies;

    private static final int POPULAR_MOVIES = 0;
    private static final int TOP_RATED_MOVIES = 1;
    private int currentSpinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainDiscoveryScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main_discovery_screen);

        setContentView(R.layout.activity_main_discovery_screen);


        rcRecyclerView = findViewById(R.id.rvMovies);
        spCriteria = findViewById(R.id.spCriteria);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        binding.setViewModel(viewModel);

        setupRecyclerView();

        setupSpinnerListener();

        initPopularMoviesObserver();
        initTopRatedMoviesObserver();

    }

    private void dispareTasks() {
        taskForPopularMovies = new MovieApiQueryTask(new MovieApiQueryTask.UpdateRecyclerView() {
            @Override
            public void onUpdate(ArrayList<Movie> results) {
                viewModel.setPopularMovies(results);
            }
        }, MovieApiQueryTask.QueryKind.POPULAR_MOVIES, getString(R.string.api_key));
        taskForTopRatedMovies = new MovieApiQueryTask(new MovieApiQueryTask.UpdateRecyclerView() {
            @Override
            public void onUpdate(ArrayList<Movie> results) {
                viewModel.setTopRatedMovies(results);
            }
        }, MovieApiQueryTask.QueryKind.TOP_RATED_MOVIES, getString(R.string.api_key));

        taskForPopularMovies.execute();
        taskForTopRatedMovies.execute();
    }


    @Override
    protected void onStart() {
        super.onStart();
        dispareTasks();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (taskForPopularMovies != null && !taskForPopularMovies.isCancelled()) {
            taskForPopularMovies.cancel(true);
        }
        if (taskForTopRatedMovies != null && !taskForTopRatedMovies.isCancelled()) {
            taskForTopRatedMovies.cancel(true);
        }

    }

    private void initPopularMoviesObserver() {
        final Observer<ArrayList<Movie>> popularMoviesObserver = new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@NonNull ArrayList<Movie> movies) {
                if (rcRecyclerView != null && currentSpinnerPosition == POPULAR_MOVIES)
                    rcRecyclerView.setAdapter(new MovieItemAdapter(movies));
            }
        };
        viewModel.getPopularMovies().observe(this, popularMoviesObserver);
    }

    private void initTopRatedMoviesObserver() {
        final Observer<ArrayList<Movie>> topRatedMoviesObserver = new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@NonNull ArrayList<Movie> movies) {
                if (rcRecyclerView != null && currentSpinnerPosition == TOP_RATED_MOVIES)
                    rcRecyclerView.setAdapter(new MovieItemAdapter(movies));
            }
        };
        viewModel.getTopRatedMovies().observe(this, topRatedMoviesObserver);
    }

    /**
     * Create the recyclerview and dispare the task to get information about movies.
     */
    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcRecyclerView.setLayoutManager(gridLayoutManager);
        rcRecyclerView.setAdapter(new MovieItemAdapter(new ArrayList<Movie>()));
    }

    private void setupSpinnerListener() {
        spCriteria.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<Movie> lstMovies = new ArrayList();
                        switch (position) {
                            case POPULAR_MOVIES: {
                                lstMovies = viewModel.getPopularMovies().getValue();
                                break;
                            }
                            case TOP_RATED_MOVIES: {
                                lstMovies = viewModel.getTopRatedMovies().getValue();
                                break;
                            }
                            default: {
                                lstMovies = viewModel.getPopularMovies().getValue();
                                break;
                            }
                        }

                        if (lstMovies != null && !lstMovies.isEmpty())
                            rcRecyclerView.setAdapter(new MovieItemAdapter(lstMovies));
                        currentSpinnerPosition = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        currentSpinnerPosition = POPULAR_MOVIES;
                    }
                }
        );
    }

    /**
     * Method called when the task has the result from request.
     *
     * @param results is a list
     */
    @Override
    public void onUpdate(ArrayList<Movie> results) {
        if (results != null && !results.equals("")) {
            if (rcRecyclerView.getAdapter() != null) {
                if (rcRecyclerView.getAdapter() instanceof MovieItemAdapter)
                    ((MovieItemAdapter) rcRecyclerView.getAdapter()).updateMovies(results);
            } else {
                rcRecyclerView.setAdapter(new MovieItemAdapter(results));
            }
        }
    }
}
