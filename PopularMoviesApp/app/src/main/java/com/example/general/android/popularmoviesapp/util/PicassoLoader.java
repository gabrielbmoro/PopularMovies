package com.example.general.android.popularmoviesapp.util;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class PicassoLoader {

    private static final String BASE_URL = "http://image.tmdb.org/t/p";

    public static void loadImageFromURL(Context context, String size, String fileName, ImageView ivReference) {
        String url = BASE_URL + "/"+ size + "/"+ fileName;
        Log.d("TESTT", "URL IMAGE : " + url);
        Picasso.with(context).load(url).into(ivReference);
    }
}
