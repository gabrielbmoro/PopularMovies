package com.example.general.android.popularmoviesapp.ui.main_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.ui.details.DetailsMovieActivity;
import com.example.general.android.popularmoviesapp.util.PicassoLoader;
import java.util.ArrayList;

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.MovieItemViewHolder>{

    private ArrayList<Movie> lstMovies;
    private Context context;

    MovieItemAdapter(@NonNull ArrayList<Movie> alstMoviesList) {
        lstMovies = alstMoviesList;
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewCreated = inflater.inflate(R.layout.movie_item, viewGroup, false);
        return new MovieItemViewHolder(viewCreated);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder movieItemViewHolder, int i) {
        Movie movieTarget = lstMovies.get(i);
        movieItemViewHolder.bind(context, movieTarget, movieTarget.getPosterPath().replaceAll("/", ""));
    }

    @Override
    public int getItemCount() {
        return lstMovies.size();
    }

    public void updateMovies(ArrayList<Movie> movies) {
        this.lstMovies.clear();
        this.lstMovies.addAll(movies);
        notifyDataSetChanged();
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {

        protected ImageButton ibMoviePoster;
        static final String IMAGE_SIZE = "w500";

        MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ibMoviePoster = itemView.findViewById(R.id.ibMoviePoster);
        }

        void bind(final Context context, final Movie movie, String fileName) {
            PicassoLoader.loadImageFromURL(context, IMAGE_SIZE, fileName, ibMoviePoster);
            /**
             * This event call the details screen.
             */
            ibMoviePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsMovieActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DetailsMovieActivity.MOVIE_INTENT_KEY, movie);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }


}
