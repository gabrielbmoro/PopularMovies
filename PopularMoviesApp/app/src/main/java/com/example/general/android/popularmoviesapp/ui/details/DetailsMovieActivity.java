package com.example.general.android.popularmoviesapp.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.databinding.ActivityDetailsViewBinding;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.Review;
import com.example.general.android.popularmoviesapp.model.VideoTrailer;

import java.util.ArrayList;

/**
 * This screen show to the user the detail about movie.
 */
public class DetailsMovieActivity extends AppCompatActivity {

    private ImageView ivPoster;
    private RecyclerView rvTrailers;
    private RecyclerView rvReviews;
    private TextView tvReviewsLabel;
    private TextView tvTrailersLabel;
    private Button btnMarkAsFavorite;

    private DetailsViewModel viewModel;

    /**
     * Key used to transfer movie from Main Screen to this screen.
     */
    public static final String MOVIE_INTENT_KEY = "MOVIE_TARGET";
    /**
     * Image size used to load the image using picasso.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailsViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_details_view);

        ivPoster = findViewById(R.id.ivPoster);
        rvTrailers = findViewById(R.id.rvTrailers);
        rvReviews = findViewById(R.id.rvReviews);
        btnMarkAsFavorite = findViewById(R.id.btnMarkAsFavorite);
        tvReviewsLabel = findViewById(R.id.tvReviewsLabel);
        tvTrailersLabel = findViewById(R.id.tvTrailersLabel);

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        Parcelable objectViaIntent = getIntent().getParcelableExtra(MOVIE_INTENT_KEY);

        if (!(objectViaIntent instanceof Movie)) finish();

        viewModel.setMovie((Movie) objectViaIntent);
        binding.setViewModel(viewModel);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTrailers.setLayoutManager(llm);
        VideoTrailerAdapter adapterTrailers = new VideoTrailerAdapter(new ArrayList<VideoTrailer>());
        rvTrailers.setAdapter(adapterTrailers);
        viewModel.setAdapterTrailers(adapterTrailers);

        LinearLayoutManager llm1 = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(llm1);
        ReviewAdapter reviewAdapter = new ReviewAdapter(new ArrayList<Review>());
        rvReviews.setAdapter(reviewAdapter);
        viewModel.setAdapterReviews(reviewAdapter);

        setupButtonToFavoriteMovie();

        loadViewModelElements();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void setupButtonToFavoriteMovie() {
        btnMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.favoriteAction(btnMarkAsFavorite);
            }
        });
    }

    private void loadViewModelElements() {
        viewModel.loadReview(new DetailsViewModel.VisibilityContract.ReviewsVisibility() {
            @Override
            public void toVisible() {
                rvReviews.setVisibility(RecyclerView.VISIBLE);
                tvReviewsLabel.setVisibility(TextView.VISIBLE);
            }

            @Override
            public void toGone() {
                rvReviews.setVisibility(RecyclerView.GONE);
                tvReviewsLabel.setVisibility(TextView.GONE);
            }
        });
        viewModel.loadTrailers(new DetailsViewModel.VisibilityContract.TrailersVisibility() {
            @Override
            public void toVisible() {
                rvTrailers.setVisibility(RecyclerView.VISIBLE);
                tvTrailersLabel.setVisibility(TextView.VISIBLE);
            }

            @Override
            public void toGone() {
                rvTrailers.setVisibility(RecyclerView.GONE);
                tvTrailersLabel.setVisibility(TextView.GONE);
            }
        });
        viewModel.loadImagePoster(ivPoster);
        viewModel.updateTheFavoriteButton(btnMarkAsFavorite);
    }

}