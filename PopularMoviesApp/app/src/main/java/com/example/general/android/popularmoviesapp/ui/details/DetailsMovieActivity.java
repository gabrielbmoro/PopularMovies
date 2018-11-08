package com.example.general.android.popularmoviesapp.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.databinding.ActivityDetailsViewBinding;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.Review;
import com.example.general.android.popularmoviesapp.model.VideoTrailer;
import com.example.general.android.popularmoviesapp.model.database.AppDatabase;
import com.example.general.android.popularmoviesapp.ui.details.reviews.ReviewAdapter;
import com.example.general.android.popularmoviesapp.ui.details.reviews.ReviewsApiQueryTask;
import com.example.general.android.popularmoviesapp.ui.details.trailers.TrailerApiQueryTask;
import com.example.general.android.popularmoviesapp.ui.details.trailers.VideoTrailerAdapter;
import com.example.general.android.popularmoviesapp.util.AppExecutors;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;
import com.example.general.android.popularmoviesapp.util.PicassoLoader;

import java.util.ArrayList;

/**
 * This screen show to the user the detail about movie.
 */
public class DetailsMovieActivity extends AppCompatActivity {

    private ImageView ivPoster;
    private RecyclerView rvTrailers;
    private RecyclerView rvReviews;
    private TextView tvTrailersLabel;
    private TextView tvReviewsLabel;
    private ScrollView svDetailsAboutMovie;
    private Button btnMarkAsFavorite;

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
        ActivityDetailsViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_details_view);

        ivPoster = findViewById(R.id.ivPoster);
        rvTrailers = findViewById(R.id.rvTrailers);
        rvReviews = findViewById(R.id.rvReviews);
        tvTrailersLabel = findViewById(R.id.tvTrailersLabel);
        tvReviewsLabel = findViewById(R.id.tvReviewsLabel);
        svDetailsAboutMovie = findViewById(R.id.svDetailsAboutMovie);
        btnMarkAsFavorite = findViewById(R.id.btnMarkAsFavorite);

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        setupRecyclerView();

        setupButtonToFavoriteMovie();;

        /**
         * Getting the movie from parcelable extras
         */
        Parcelable objectViaIntent = getIntent().getParcelableExtra(MOVIE_INTENT_KEY);

        if (!(objectViaIntent instanceof Movie)) finish();

        viewModel.setMovie((Movie) objectViaIntent);
        binding.setViewModel(viewModel);
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

        updateFavoriteButtonStatus();
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
        if (movieTarget != null && NetworkUtils.hasInternetConnection(this)) {
            requestForTrailers = new TrailerApiQueryTask(getString(R.string.api_key), movieTarget.getId(), new TrailerApiQueryTask.UpdateRecyclerView() {
                @Override
                public void onUpdate(ArrayList<VideoTrailer> results) {
                    viewModel.setTrailerLst(results);
                    if (results == null || results.isEmpty()) {
                        tvTrailersLabel.setVisibility(TextView.GONE);
                        rvTrailers.setVisibility(RecyclerView.GONE);
                    } else {
                        tvTrailersLabel.setVisibility(TextView.VISIBLE);
                        rvTrailers.setVisibility(RecyclerView.VISIBLE);
                    }
                    requestForReviews.execute();
                }
            });
            requestForReviews = new ReviewsApiQueryTask(getString(R.string.api_key), movieTarget.getId(), new ReviewsApiQueryTask.UpdateRecyclerView() {
                @Override
                public void onUpdate(ArrayList<Review> results) {
                    viewModel.setReviews(results);
                    if (results == null || results.isEmpty()) {
                        tvReviewsLabel.setVisibility(TextView.GONE);
                        rvReviews.setVisibility(RecyclerView.GONE);
                    } else {
                        tvReviewsLabel.setVisibility(TextView.VISIBLE);
                        rvReviews.setVisibility(RecyclerView.VISIBLE);
                    }
                    svDetailsAboutMovie.scrollTo(0, 0);
                }
            });
            requestForTrailers.execute();
        } else {
            Toast.makeText(this, getString(R.string.messageConnection), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method shows all information
     */
    private void loadInfo() {
        Movie movieTarget = viewModel.getMovie().getValue();
        if (movieTarget != null) {
            PicassoLoader.loadImageFromURL(this, IMAGE_SIZE, movieTarget.getPosterPath().replaceAll("/", ""), ivPoster);
        }
    }

    private void updateFavoriteButtonStatus() {
        final Context context = this;
        AppExecutors.getInstance()
                .getDiskIO()
                .execute(new Runnable() {
                             @Override
                             public void run() {
                                 if (viewModel.getMovie().getValue() != null) {
                                     if (checkIfMovieIsAFavorite(context, viewModel.getMovie().getValue())) {
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 btnMarkAsFavorite.setText(R.string.markOffFavorite);
                                             }
                                         });
                                     } else {
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 btnMarkAsFavorite.setText(R.string.markAsFavorite);
                                             }
                                         });
                                     }
                                 }
                             }
                         }
                );
    }


    private void setupButtonToFavoriteMovie() {
        final Context context = this;

        btnMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Movie movie = viewModel.getMovie().getValue();

                AppExecutors.getInstance().getDiskIO().execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                assert movie != null;
                                boolean isAFavorite = checkIfMovieIsAFavorite(context, movie);
                                if (!isAFavorite) {
                                    movie.setFavorite(true);
                                    AppDatabase.getInstance(context).movieDao().insertMovie(movie);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnMarkAsFavorite.setText(R.string.markOffFavorite);
                                        }
                                    });
                                } else {
                                    AppDatabase.getInstance(context).movieDao().deleteMovie(movie);
                                    movie.setFavorite(false);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnMarkAsFavorite.setText(R.string.markAsFavorite);
                                        }
                                    });
                                }
                            }
                        });

            }
        });
    }

    public boolean checkIfMovieIsAFavorite(Context context, @NonNull Movie movie) {
        return (!AppDatabase.getInstance(context).movieDao().getSomeFavoriteMovieAccordingId(movie.getId()).isEmpty());
    }
}

