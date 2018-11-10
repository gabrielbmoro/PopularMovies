package com.example.general.android.popularmoviesapp.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * This class loads the image from url
 * Reference: https://medium.com/@crossphd/android-image-loading-from-a-string-url-6c8290b82c5e
 */
public class PicassoLoader {

    private static final String BASE_URL = "http://image.tmdb.org/t/p";

    public static void loadImageFromURL(Context context, String size, String fileName, ImageView ivReference) {
        String url = BASE_URL + "/" + size + "/" + fileName;
        Picasso.with(context).load(url).into(ivReference);
    }
}
