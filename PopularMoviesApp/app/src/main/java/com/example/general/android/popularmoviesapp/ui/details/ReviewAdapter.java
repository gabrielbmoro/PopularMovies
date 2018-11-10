package com.example.general.android.popularmoviesapp.ui.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.general.android.popularmoviesapp.R;
import com.example.general.android.popularmoviesapp.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> lstReviews;

    ReviewAdapter(ArrayList<Review> alstReviewList) {
        lstReviews = alstReviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewCreated = inflater.inflate(R.layout.review_item, viewGroup, false);
        return new ReviewViewHolder(viewCreated);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        Review reviewTarget = lstReviews.get(i);
        reviewViewHolder.bind(reviewTarget);
    }

    @Override
    public int getItemCount() {
        return lstReviews.size();
    }

    public void updateReviews(List<Review> reviewList) {
        this.lstReviews.clear();
        this.lstReviews.addAll(reviewList);
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuthor;
        TextView tvContent;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
        }

        void bind(final Review review) {
            tvAuthor.setText(review.getAuthor());
            tvContent.setText(review.getContent());
        }
    }
}