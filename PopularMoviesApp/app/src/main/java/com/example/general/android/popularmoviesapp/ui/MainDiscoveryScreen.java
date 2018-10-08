package com.example.general.android.popularmoviesapp.ui;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.util.JSONParser;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainDiscoveryScreen extends AppCompatActivity {

    protected RecyclerView rcRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_discovery_screen);

        rcRecyclerView = findViewById(R.id.rvMovies);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupRecyclerView();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcRecyclerView.setLayoutManager(gridLayoutManager);
        new MovieApiQueryTask().execute(NetworkUtils.buildURLToAccessMovies(getString(R.string.api_key), "popular"));

    }

    class MovieApiQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            URL searchURL = params[0];
            ArrayList<Movie> results = null;
            try {
                results = JSONParser.gettingResultsValues(
                        NetworkUtils.getResponseFromHttpURL(searchURL)
                );

            } catch (IOException e){
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> results) {
            if(results != null && !results.equals("")) {
                rcRecyclerView.setAdapter(new MovieItemAdapter(results));
            }
        }
    }
}
