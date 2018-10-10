package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;
import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;
import java.util.ArrayList;

/**
 * Main screen
 */
public class MainDiscoveryScreen extends AppCompatActivity implements MovieApiQueryTask.UpdateRecyclerView {

    protected RecyclerView rcRecyclerView;
    protected Spinner spCriteria;
    /**
     * Task responsable for require information to the api
     */
    private MovieApiQueryTask updateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_discovery_screen);

        rcRecyclerView = findViewById(R.id.rvMovies);
        spCriteria = findViewById(R.id.spCriteria);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupRecyclerView();
        setupSpinnerListener();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(updateTask!=null) {
            updateTask.cancel(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Create the recyclerview and dispare the task to get information about movies.
     */
    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcRecyclerView.setLayoutManager(gridLayoutManager);
        if(updateTask!=null) {
            updateTask.cancel(true);
            updateTask = null;
        }
        updateTask = new MovieApiQueryTask(this);
        updateTask.execute(NetworkUtils.buildURLToAccessMovies(getString(R.string.api_key), "popular"));
    }

    private void setupSpinnerListener() {
        spCriteria.setOnItemSelectedListener(
                new CustomOnSortMovieSelectedListener(updateTask, this, this)
        );
    }

    /**
     * Method called when the task has the result from request.
     * @param results is a list
     */
    @Override
    public void onUpdate(ArrayList<Movie> results) {
        if(results != null && !results.equals("")) {
            if(rcRecyclerView.getAdapter() != null) {
                if(rcRecyclerView.getAdapter() instanceof MovieItemAdapter)
                    ((MovieItemAdapter) rcRecyclerView.getAdapter()).updateMovies(results);
            } else {
                rcRecyclerView.setAdapter(new MovieItemAdapter(results));
            }
        }
    }
}
