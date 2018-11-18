package com.example.general.android.popularmoviesapp.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.databinding.ActivityDetailsViewBinding;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.Review;
import com.example.general.android.popularmoviesapp.model.VideoTrailer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This screen show to the user the detail about movie.
 */
public class DetailsMovieActivity extends AppCompatActivity {

    @BindView(R.id.ivPoster)
    ImageView ivPoster;
    @BindView(R.id.rvTrailers)
    RecyclerView rvTrailers;
    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;
    @BindView(R.id.tvReviewsLabel)
    TextView tvReviewsLabel;
    @BindView(R.id.tvTrailersLabel)
    TextView tvTrailersLabel;
    @BindView(R.id.tvReleaseDate)
    TextView tvReleaseDate;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvUserRating)
    TextView tvUserRating;
    @BindView(R.id.tvOverview)
    TextView tvOverview;
    @BindView(R.id.btnMarkAsFavorite)
    Button btnMarkAsFavorite;

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
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        binding.setViewModel(viewModel);

        ButterKnife.bind(this);

        if (!getIntent().hasExtra(MOVIE_INTENT_KEY)) finish();

        Parcelable objectViaIntent = getIntent().getParcelableExtra(MOVIE_INTENT_KEY);
        if (!(objectViaIntent instanceof Movie)) finish();

        final Movie movieTarget = (Movie) objectViaIntent;

        viewModel.initLiveData(movieTarget, this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie target) {
                if (target != null) {
                    if (target.isFavorite()) {
                        btnMarkAsFavorite.setText(R.string.markOffFavorite);
                    } else {
                        btnMarkAsFavorite.setText(R.string.markAsFavorite);
                    }
                }
            }
        });

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

        loadInitialInformation(movieTarget);
        loadViewModelElements();
    }

    private void loadInitialInformation(Movie objectViaIntent) {
        if (objectViaIntent == null) return;

        tvReleaseDate.setText(objectViaIntent.getReleaseDate());
        String userRatingValue = objectViaIntent.getVoteAverage() + "/10";
        tvUserRating.setText(userRatingValue);
        tvTitle.setText(objectViaIntent.getTitle());
        tvOverview.setText(objectViaIntent.getOverview());
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

    @OnClick(R.id.btnMarkAsFavorite)
    void onFavoriteEvent() { viewModel.favoriteAction(); }

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