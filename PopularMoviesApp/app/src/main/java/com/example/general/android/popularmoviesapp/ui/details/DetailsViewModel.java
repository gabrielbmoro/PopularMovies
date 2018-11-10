package com.example.general.android.popularmoviesapp.ui.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.model.Review;
import com.example.general.android.popularmoviesapp.model.VideoTrailer;
import com.example.general.android.popularmoviesapp.model.database.AppDatabase;
import com.example.general.android.popularmoviesapp.ui.details.tasks.ReviewsApiQueryTask;
import com.example.general.android.popularmoviesapp.ui.details.tasks.TrailerApiQueryTask;
import com.example.general.android.popularmoviesapp.util.AppExecutors;
import com.example.general.android.popularmoviesapp.util.MathService;
import com.example.general.android.popularmoviesapp.util.NetworkUtils;
import com.example.general.android.popularmoviesapp.util.PicassoLoader;

import java.util.ArrayList;

public class DetailsViewModel extends AndroidViewModel {

    private MutableLiveData<Movie> movie = new MutableLiveData<>();
    private MutableLiveData<ArrayList<VideoTrailer>> trailerLst = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Review>> reviewsLst = new MutableLiveData<>();

    /**
     * Tasks
     */
    private TrailerApiQueryTask requestForTrailers;
    private ReviewsApiQueryTask requestForReviews;

    private VideoTrailerAdapter adapterTrailers;
    private ReviewAdapter adapterReviews;

    private String apiKey;

    public DetailsViewModel(@NonNull Application application) {
        super(application);

        this.apiKey = application.getResources().getString(R.string.api_key);
    }

    public void setMovie(@NonNull Movie movieTarget) {
        movie.setValue(movieTarget);
    }

    void loadReview(final VisibilityContract.ReviewsVisibility contract) {
        if (reviewsLst.getValue() == null || (reviewsLst.getValue() != null && reviewsLst.getValue().isEmpty())) {
            if (NetworkUtils.hasInternetConnection(getApplication()) && movie.getValue() != null) {
                requestForReviews = new ReviewsApiQueryTask(apiKey, movie.getValue().getId(), new ReviewsApiQueryTask.UpdateRecyclerView() {
                    @Override
                    public void onUpdate(ArrayList<Review> results) {
                        reviewsLst.setValue(results);
                        adapterReviews.updateReviews(results);
                        if(adapterReviews.getItemCount() > 0) contract.toVisible(); else contract.toGone();
                    }
                });
                requestForReviews.execute();
            }
        } else {
            adapterReviews.updateReviews(reviewsLst.getValue());
            if(adapterReviews.getItemCount() > 0) {
                contract.toVisible();
            } else {
                contract.toGone();
            }
        }
    }

    void loadTrailers(final VisibilityContract.TrailersVisibility contract) {
        if (trailerLst.getValue() == null || (trailerLst.getValue() != null && trailerLst.getValue().isEmpty())) {
            if (NetworkUtils.hasInternetConnection(getApplication())){
                requestForTrailers = new TrailerApiQueryTask(apiKey, movie.getValue().getId(), new TrailerApiQueryTask.UpdateRecyclerView() {
                    @Override
                    public void onUpdate(ArrayList<VideoTrailer> results) {
                        trailerLst.setValue(results);
                        adapterTrailers.updateTrailers(results);
                        if(adapterTrailers.getItemCount() > 0) contract.toVisible(); else contract.toGone();
                    }
                });
                requestForTrailers.execute();

            }
        } else {
            adapterTrailers.updateTrailers(trailerLst.getValue());
            if(adapterTrailers.getItemCount() > 0) {
                contract.toVisible();
            } else {
                contract.toGone();
            }
        }
    }

    void loadImagePoster(ImageView imageView, String imageSize) {
        PicassoLoader.loadImageFromURL(getApplication(), imageSize, movie.getValue().getPosterPath().replaceAll("/", ""), imageView);
    }

    void updateTheFavoriteButton(final Button favoriteButton) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (movie.getValue() != null) {
                    boolean isThereSomeFavoriteMovie = AppDatabase.getInstance(getApplication()).movieDao().getSomeFavoriteMovieAccordingId(movie.getValue().getId()).size() > 0;
                    if (isThereSomeFavoriteMovie) {
                        favoriteButton.setText(R.string.markOffFavorite);
                        movie.getValue().setFavorite(true);
                    } else {
                        favoriteButton.setText(R.string.markAsFavorite);
                        movie.getValue().setFavorite(false);
                    }
                }
            }
        });
    }

    void favoriteAction(final Button favoriteButton) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (movie.getValue() != null) {
                    if (movie.getValue().isFavorite()) {
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                AppDatabase.getInstance(getApplication()).movieDao().deleteMovie(movie.getValue());
                                movie.getValue().setFavorite(false);
                                AppExecutors.getInstance().getMainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        favoriteButton.setText(R.string.markAsFavorite);
                                    }
                                });

                            }
                        });
                    } else {
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                movie.getValue().setFavorite(true);
                                AppDatabase.getInstance(getApplication()).movieDao().insertMovie(movie.getValue());
                                AppExecutors.getInstance().getMainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        favoriteButton.setText(R.string.markOffFavorite);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }


    public MutableLiveData<Movie> getMovie() {
        return movie;
    }

    public String getMovieTitle() {
        return movie.getValue().getTitle();
    }

    public String getMovieReleaseDate() {
        return MathService.getYearFromDate(movie.getValue().getReleaseDate());
    }

    public String getMovieUserRating() {
        return movie.getValue().getVoteAverage() + "/10";
    }

    public String getMovieOverview() {
        return movie.getValue().getOverview();
    }

    void setAdapterTrailers(VideoTrailerAdapter adapterTrailers) {
        this.adapterTrailers = adapterTrailers;
    }

    void setAdapterReviews(ReviewAdapter adapterReviews) {
        this.adapterReviews = adapterReviews;
    }

    public interface VisibilityContract {
        interface TrailersVisibility{
            void toVisible();
            void toGone();
        }
        interface ReviewsVisibility{
            void toVisible();
            void toGone();
        }
    }

}

