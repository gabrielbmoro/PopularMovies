package com.example.general.android.popularmoviesapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.util.PicassoLoader;

public class DetailsView extends AppCompatActivity {

    public static final String MOVIE_INTENT_KEY = "MOVIE_TARGET";
    private Movie movie;
    private ImageView ivPoster;
    private TextView tvReleaseDate;
    private TextView tvTitle;
    private TextView tvUserRating;
    private TextView tvDuration;
    static final String IMAGE_SIZE = "w342";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        ivPoster = findViewById(R.id.ivPoster);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvTitle = findViewById(R.id.tvTitle);
        tvUserRating = findViewById(R.id.tvUserRating);
        tvDuration = findViewById(R.id.tvDuration);

        movie = getIntent().getParcelableExtra(MOVIE_INTENT_KEY);
        if(movie==null) finish();

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
    }

    private void loadInfo(){
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvUserRating.setText(String.valueOf(movie.getVoteAverage()));
        PicassoLoader.loadImageFromURL(this, IMAGE_SIZE, movie.getPosterPath().replaceAll("/", ""), ivPoster);
    }
}
