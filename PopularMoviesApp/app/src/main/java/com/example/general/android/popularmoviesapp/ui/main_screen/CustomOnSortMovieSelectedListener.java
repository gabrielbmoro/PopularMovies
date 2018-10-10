package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;

public class CustomOnSortMovieSelectedListener implements AdapterView.OnItemSelectedListener {

    private MovieApiQueryTask task;
    private MovieApiQueryTask.UpdateRecyclerView baseContract;
    private Context contextReference;

    CustomOnSortMovieSelectedListener(MovieApiQueryTask updateTask,
                                      MovieApiQueryTask.UpdateRecyclerView contract,
                                      Context context) {
        task = updateTask;
        contextReference = context;
        baseContract = contract;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(task != null) task.cancel(true);
        switch (position) {
            case 0:
                task = new MovieApiQueryTask(baseContract);
                task.execute(NetworkUtils.buildURLToAccessMovies(contextReference.getString(R.string.api_key), "popular"));
                break;
            case 1:
                task = new MovieApiQueryTask(baseContract);
                task.execute(NetworkUtils.buildURLToAccessMovies(contextReference.getString(R.string.api_key), "top_rated"));
                break;
            default:
                task = new MovieApiQueryTask(baseContract);
                task.execute(NetworkUtils.buildURLToAccessMovies(contextReference.getString(R.string.api_key), "popular"));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
