package com.example.general.android.popularmoviesapp.ui.details;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.VideoTrailer;

import java.util.ArrayList;


public class VideoTrailerAdapter extends RecyclerView.Adapter<VideoTrailerAdapter.VideoTrailerViewHolder> {

    private ArrayList<VideoTrailer> lstTrailers;
    private Context context;

    VideoTrailerAdapter(ArrayList<VideoTrailer> alstMoviesList) {
        lstTrailers = alstMoviesList;
    }

    @NonNull
    @Override
    public VideoTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewCreated = inflater.inflate(R.layout.trailer_item, viewGroup, false);
        return new VideoTrailerViewHolder(viewCreated);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoTrailerViewHolder videoTrailerViewHolder, int i) {
        VideoTrailer trailerTarget = lstTrailers.get(i);
        videoTrailerViewHolder.bind(context, trailerTarget);
    }

    @Override
    public int getItemCount() {
        return lstTrailers.size();
    }

    class VideoTrailerViewHolder extends RecyclerView.ViewHolder {

        TextView tvTrailerLabel;
        ImageView imageView;

        VideoTrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrailerLabel = itemView.findViewById(R.id.tvTrailerLabel);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void bind(final Context context, final VideoTrailer trailer) {
            tvTrailerLabel.setText(trailer.getName());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(appIntent);
                }
            });
        }
    }
}
