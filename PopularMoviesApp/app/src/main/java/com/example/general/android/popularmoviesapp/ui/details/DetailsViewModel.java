package com.example.general.android.popularmoviesapp.ui.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;

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
                new ReviewsApiQueryTask(apiKey, movie.getValue().getId(), new ReviewsApiQueryTask.UpdateRecyclerView() {
                    @Override
                    public void onUpdate(ArrayList<Review> results) {
                        reviewsLst.setValue(results);
                        adapterReviews.updateReviews(results);
                        if (adapterReviews.getItemCount() > 0) contract.toVisible();
                        else contract.toGone();
                    }
                }).execute();
            }
        } else {
            adapterReviews.updateReviews(reviewsLst.getValue());
            if (adapterReviews.getItemCount() > 0) {
                contract.toVisible();
            } else {
                contract.toGone();
            }
        }
    }

    void loadTrailers(final VisibilityContract.TrailersVisibility contract) {
        if (trailerLst.getValue() == null || (trailerLst.getValue() != null && trailerLst.getValue().isEmpty())) {
            if (NetworkUtils.hasInternetConnection(getApplication())) {
                if (movie.getValue() != null) {
                    new TrailerApiQueryTask(apiKey, movie.getValue().getId(), new TrailerApiQueryTask.UpdateRecyclerView() {
                        @Override
                        public void onUpdate(ArrayList<VideoTrailer> results) {
                            trailerLst.setValue(results);
                            adapterTrailers.updateTrailers(results);
                            if (adapterTrailers.getItemCount() > 0) contract.toVisible();
                            else contract.toGone();
                        }
                    }).execute();
                }

            }
        } else {
            adapterTrailers.updateTrailers(trailerLst.getValue());
            if (adapterTrailers.getItemCount() > 0) {
                contract.toVisible();
            } else {
                contract.toGone();
            }
        }
    }

    void loadImagePoster(ImageView imageView) {
        final String imageSize = "w342";
        if (NetworkUtils.hasInternetConnection(getApplication())) {
            if (movie.getValue() != null)
                PicassoLoader.loadImageFromURL(getApplication(), imageSize, movie.getValue().getPosterPath().replaceAll("/", ""), imageView);
        } else {
            imageView.setImageResource(R.drawable.no_connection);
        }
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
        if (movie.getValue() != null)
            return movie.getValue().getTitle();
        else return "";
    }

    public String getMovieReleaseDate() {
        if (movie.getValue() != null)
            return MathService.getYearFromDate(movie.getValue().getReleaseDate());
        else return "";
    }

    public String getMovieUserRating() {
        if (movie.getValue() != null) {
            return movie.getValue().getVoteAverage() + "/10";
        } else {
            return "";
        }
    }

    public String getMovieOverview() {
        if (movie.getValue() != null)
            return movie.getValue().getOverview();
        else return "";
    }

    void setAdapterTrailers(VideoTrailerAdapter adapterTrailers) {
        this.adapterTrailers = adapterTrailers;
    }

    void setAdapterReviews(ReviewAdapter adapterReviews) {
        this.adapterReviews = adapterReviews;
    }

    public interface VisibilityContract {
        interface TrailersVisibility {
            void toVisible();

            void toGone();
        }

        interface ReviewsVisibility {
            void toVisible();

            void toGone();
        }
    }

}

