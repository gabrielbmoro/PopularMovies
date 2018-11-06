package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.databinding.ActivityMainDiscoveryScreenBinding;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.database.AppDatabase;
import com.example.general.android.popularmoviesapp.util.AppExecutors;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Main screen
 */
public class MainActivity extends AppCompatActivity implements MovieApiQueryTask.UpdateRecyclerView, SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerView rcRecyclerView;
    protected Spinner spCriteria;
    protected SwipeRefreshLayout srlMovies;
    private MainViewModel viewModel;
    /**
     * Task responsable for require information to the api
     */
    private MovieApiQueryTask taskForPopularMovies;
    private MovieApiQueryTask taskForTopRatedMovies;

    private static final int POPULAR_MOVIES = 0;
    private static final int TOP_RATED_MOVIES = 1;
    private static final int FAVORITE_MOVIES = 2;
    private int currentSpinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainDiscoveryScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main_discovery_screen);

        setContentView(R.layout.activity_main_discovery_screen);


        rcRecyclerView = findViewById(R.id.rvMovies);
        spCriteria = findViewById(R.id.spCriteria);
        srlMovies = findViewById(R.id.srlMovies);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        binding.setViewModel(viewModel);

        setupRecyclerView();

        setupSwipeRefreshLayout();

        setupSpinnerListener();

        initPopularMoviesObserver();
        initTopRatedMoviesObserver();

    }

    private void dispareTasks() {
        if (NetworkUtils.hasInternetConnection(this)) {
            taskForPopularMovies = new MovieApiQueryTask(new MovieApiQueryTask.UpdateRecyclerView() {
                @Override
                public void onUpdate(ArrayList<Movie> results) {
                    viewModel.setPopularMovies(results);
                    taskForTopRatedMovies.execute();
                }
            }, MovieApiQueryTask.QueryKind.POPULAR_MOVIES, getString(R.string.api_key));
            taskForTopRatedMovies = new MovieApiQueryTask(new MovieApiQueryTask.UpdateRecyclerView() {
                @Override
                public void onUpdate(ArrayList<Movie> results) {
                    viewModel.setTopRatedMovies(results);
                    srlMovies.setRefreshing(false);
                }
            }, MovieApiQueryTask.QueryKind.TOP_RATED_MOVIES, getString(R.string.api_key));
            taskForPopularMovies.execute();
        } else {
            Toast.makeText(this, getString(R.string.messageConnection), Toast.LENGTH_SHORT).show();
            srlMovies.setRefreshing(false);
        }
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

    private void setupSwipeRefreshLayout() {
        srlMovies.setOnRefreshListener(this);
    }

    private void setupSpinnerListener() {
        final Context context = this;
        spCriteria.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case POPULAR_MOVIES: {
                                if (viewModel.getPopularMovies().getValue() != null && !viewModel.getPopularMovies().getValue().isEmpty())
                                    rcRecyclerView.setAdapter(new MovieItemAdapter(viewModel.getPopularMovies().getValue()));
                                break;
                            }
                            case TOP_RATED_MOVIES: {
                                if (viewModel.getTopRatedMovies().getValue() != null && !viewModel.getTopRatedMovies().getValue().isEmpty())
                                    rcRecyclerView.setAdapter(new MovieItemAdapter(viewModel.getTopRatedMovies().getValue()));
                                break;
                            }
                            case FAVORITE_MOVIES: {
                                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<Movie> lst = AppDatabase.getInstance(context).movieDao().loadAllFavoriteMovies();
                                        if (lst != null && !lst.isEmpty())
                                            rcRecyclerView.setAdapter(new MovieItemAdapter(lst));
                                    }
                                });
                                break;
                            }
                            default: {
                                if (viewModel.getPopularMovies().getValue() != null && !viewModel.getPopularMovies().getValue().isEmpty())
                                    rcRecyclerView.setAdapter(new MovieItemAdapter(viewModel.getPopularMovies().getValue()));
                                break;
                            }
                        }


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

    @Override
    public void onRefresh() {
        viewModel.clearMovieLists();
        rcRecyclerView.getAdapter().notifyDataSetChanged();
        dispareTasks();
    }
}
