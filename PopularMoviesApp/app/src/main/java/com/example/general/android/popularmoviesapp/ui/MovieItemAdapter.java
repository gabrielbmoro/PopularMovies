package com.example.general.android.popularmoviesapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Movie;
import com.example.general.android.popularmoviesapp.util.PicassoLoader;
import java.util.ArrayList;

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.MovieItemViewHolder>{

    private ArrayList<Movie> lstMovies;
    private Context context;

    MovieItemAdapter(ArrayList<Movie> alstMoviesList) {
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
        movieItemViewHolder.bind(context, lstMovies.get(i).getPosterPath().replaceAll("/", ""));
    }

    @Override
    public int getItemCount() {
        return lstMovies.size();
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {

        protected ImageButton ibMoviePoster;
        static final String IMAGE_SIZE = "w342";

        MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ibMoviePoster = itemView.findViewById(R.id.ibMoviePoster);
        }

        void bind(Context context, String fileName) {
            PicassoLoader.loadImageFromURL(context, IMAGE_SIZE, fileName, ibMoviePoster);
        }
    }


}
