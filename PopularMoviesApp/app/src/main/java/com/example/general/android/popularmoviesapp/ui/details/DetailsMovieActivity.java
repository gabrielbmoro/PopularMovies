package com.example.general.android.popularmoviesapp.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private DetailsViewModel viewModel;
    /**
     * Tasks
     */
    private TrailerApiQueryTask requestForTrailers;
    private ReviewsApiQueryTask requestForReviews;

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

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        setupRecyclerView();

        /**
         * Getting the movie from parcelable extras
         */
        Parcelable objectViaIntent = getIntent().getParcelableExtra(MOVIE_INTENT_KEY);

        if (!(objectViaIntent instanceof Movie)) finish();

        viewModel.setMovie((Movie) objectViaIntent);

        initReviewsObserver();
        initTrailersObserver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dispareTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
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


    private void setupRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTrailers.setLayoutManager(llm);
        LinearLayoutManager llm1 = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(llm1);
    }


    private void initReviewsObserver() {
        final Observer<ArrayList<Review>> reviewsObserver = new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(@NonNull ArrayList<Review> reviews) {
                if (rvReviews != null) {
                    rvReviews.setAdapter(new ReviewAdapter(reviews));
                }
            }
        };
        viewModel.getReviewsLst().observe(this, reviewsObserver);
    }

    private void initTrailersObserver() {
        final Observer<ArrayList<VideoTrailer>> trailersObserver = new Observer<ArrayList<VideoTrailer>>() {
            @Override
            public void onChanged(@Nullable ArrayList<VideoTrailer> videoTrailers) {
                if (rvTrailers != null) {
                    rvTrailers.setAdapter(new VideoTrailerAdapter(videoTrailers));
                }
            }
        };
        viewModel.getTrailerLst().observe(this, trailersObserver);
    }

    private void dispareTasks() {
        Movie movieTarget = viewModel.getMovie().getValue();
        if (movieTarget != null) {
            requestForReviews = new ReviewsApiQueryTask(getString(R.string.api_key), movieTarget.getId(), new ReviewsApiQueryTask.UpdateRecyclerView() {
                @Override
                public void onUpdate(ArrayList<Review> results) {
                    viewModel.setReviews(results);
                }
            });
            requestForTrailers = new TrailerApiQueryTask(getString(R.string.api_key), movieTarget.getId(), new TrailerApiQueryTask.UpdateRecyclerView() {
                @Override
                public void onUpdate(ArrayList<VideoTrailer> results) {
                    viewModel.setTrailerLst(results);
                }
            });

            requestForReviews.execute();
            requestForTrailers.execute();
        }
    }

    /**
     * This method shows all information
     */
    private void loadInfo() {
        Movie movieTarget = viewModel.getMovie().getValue();
        if (movieTarget != null) {
            tvTitle.setText(movieTarget.getTitle());
            tvReleaseDate.setText(MathService.getYearFromDate(movieTarget.getReleaseDate()));
            String userRatingFormatted = movieTarget.getVoteAverage() + "/10";
            tvUserRating.setText(userRatingFormatted);
            tvOverview.setText(movieTarget.getOverview());
            PicassoLoader.loadImageFromURL(this, IMAGE_SIZE, movieTarget.getPosterPath().replaceAll("/", ""), ivPoster);
        }
    }
}
