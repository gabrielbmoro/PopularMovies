package com.example.general.android.popularmoviesapp.ui.details;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.Review;
import com.example.general.android.popularmoviesapp.model.VideoTrailer;
import com.example.general.android.popularmoviesapp.ui.details.reviews.ReviewAdapter;
import com.example.general.android.popularmoviesapp.ui.details.reviews.ReviewsApiQueryTask;
import com.example.general.android.popularmoviesapp.ui.details.trailers.TrailerApiQueryTask;
import com.example.general.android.popularmoviesapp.ui.details.trailers.VideoTrailerAdapter;
import com.example.general.android.popularmoviesapp.util.MathService;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;
import com.example.general.android.popularmoviesapp.util.PicassoLoader;

import java.util.ArrayList;

/**
 * This screen show to the user the detail about movie.
 */
public class DetailsMovieActivity extends AppCompatActivity {

    private ImageView ivPoster;
    private TextView tvReleaseDate;
    private TextView tvTitle;
    private TextView tvUserRating;
    private me.grantland.widget.AutofitTextView tvOverview;
    private RecyclerView rvTrailers;
    private RecyclerView rvReviews;
    /**
     * Tasks
     */
    private TrailerApiQueryTask requestForTrailers;
    private ReviewsApiQueryTask requestForReviews;

    /**
     * Movie choosed
     */
    private Movie movie;
    /**
     * Key used to transfer movie from Main Screen to this screen.
     */
    public static final String MOVIE_INTENT_KEY = "MOVIE_TARGET";
    /**
     * Image size used to load the image using picasso.
     */
    static final String IMAGE_SIZE = "w342";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        ivPoster = findViewById(R.id.ivPoster);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvTitle = findViewById(R.id.tvTitle);
        tvUserRating = findViewById(R.id.tvUserRating);
        tvOverview = findViewById(R.id.tvOverview);
        rvTrailers = findViewById(R.id.rvTrailers);
        rvReviews = findViewById(R.id.rvReviews);

        /**
         * Getting the movie from parcelable extras
         */
        movie = getIntent().getParcelableExtra(MOVIE_INTENT_KEY);

        if (movie == null) finish();

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
        loadTrailers();
        loadReviews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestForTrailers != null) {
            requestForTrailers.cancel(true);
        }
        if (requestForReviews != null) {
            requestForReviews.cancel(true);
        }
    }

    private void loadTrailers() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTrailers.setLayoutManager(llm);

        if (requestForTrailers != null) {
            requestForTrailers.cancel(true);
            requestForTrailers = null;
        }
        requestForTrailers = new TrailerApiQueryTask(new TrailerApiQueryTask.UpdateRecyclerView() {
            @Override
            public void onUpdate(ArrayList<VideoTrailer> results) {
                rvTrailers.setAdapter(new VideoTrailerAdapter(results));
            }
        });
        requestForTrailers.execute(NetworkUtils.buildURLToFetchTrailers(getString(R.string.api_key), movie.getId()));
    }

    private void loadReviews() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(llm);

        if (requestForReviews != null) {
            requestForReviews.cancel(true);
            requestForReviews = null;
        }
        requestForReviews = new ReviewsApiQueryTask(new ReviewsApiQueryTask.UpdateRecyclerView() {
            @Override
            public void onUpdate(ArrayList<Review> results) {
                rvReviews.setAdapter(new ReviewAdapter((results)));
            }
        });
        requestForReviews.execute(NetworkUtils.buildURLToFetchReviews(getString(R.string.api_key), movie.getId()));
    }

    /**
     * This method shows all information
     */
    private void loadInfo() {
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(MathService.getYearFromDate(movie.getReleaseDate()));
        String userRatingFormatted = movie.getVoteAverage() + "/10";
        tvUserRating.setText(userRatingFormatted);
        tvOverview.setText(movie.getOverview());
        PicassoLoader.loadImageFromURL(this, IMAGE_SIZE, movie.getPosterPath().replaceAll("/", ""), ivPoster);
    }
}
