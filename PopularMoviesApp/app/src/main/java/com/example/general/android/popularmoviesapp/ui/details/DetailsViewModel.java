package com.example.general.android.popularmoviesapp.ui.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.example.general.android.popularmoviesapp.util.NetworkUtils;
import com.example.general.android.popularmoviesapp.util.PicassoLoader;

import java.util.ArrayList;

public class DetailsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<VideoTrailer>> trailerLst = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Review>> reviewsLst = new MutableLiveData<>();

    private LiveData<Movie> movie;
    private Long nMovieId;
    private String posterPath;

    private VideoTrailerAdapter adapterTrailers;
    private ReviewAdapter adapterReviews;

    private String apiKey;

    public DetailsViewModel(@NonNull Application application) {
        super(application);

        this.apiKey = application.getResources().getString(R.string.api_key);
    }

    void initLiveData(final @NonNull Movie movieTarget, final AppCompatActivity activity, final Observer<Movie> contract) {
        nMovieId = movieTarget.getId();
        posterPath = movieTarget.getPosterPath();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(AppDatabase.getInstance(getApplication()).movieDao().getMovieAccordingToId(nMovieId)==null) {
                    AppDatabase.getInstance(getApplication()).movieDao().saveMovie(movieTarget);
                }
                movie = AppDatabase.getInstance(getApplication()).movieDao().getLiveDataToFavoriteMovieAccordingId(movieTarget.getId());
                movie.observe(activity, contract);
            }
        });
    }

    void loadReview(final VisibilityContract.ReviewsVisibility contract) {
        if (reviewsLst.getValue() == null || (reviewsLst.getValue() != null && reviewsLst.getValue().isEmpty())) {
            if (NetworkUtils.hasInternetConnection(getApplication())) {
                new ReviewsApiQueryTask(apiKey, nMovieId, new ReviewsApiQueryTask.UpdateRecyclerView() {
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
                new TrailerApiQueryTask(apiKey, nMovieId, new TrailerApiQueryTask.UpdateRecyclerView() {
                    @Override
                    public void onUpdate(ArrayList<VideoTrailer> results) {
                        trailerLst.setValue(results);
                        adapterTrailers.updateTrailers(results);
                        if (adapterTrailers.getItemCount() > 0) contract.toVisible();
                        else contract.toGone();
                    }
                }).execute();
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
            PicassoLoader.loadImageFromURL(getApplication(), imageSize, posterPath.replaceAll("/", ""), imageView);
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

    void favoriteAction() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (movie.getValue() != null) {
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movie.getValue().setFavorite(!movie.getValue().isFavorite());
                            AppDatabase.getInstance(getApplication()).movieDao().saveMovie(movie.getValue());
                        }
                    });
                }
            }
        });
    }


    public LiveData<Movie> getMovie() {
        return movie;
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

