package com.example.general.android.popularmoviesapp.util;

/**
 * This class will be used to provide service for all elements of this project.
 */
public class MathService {

    public static String getYearFromDate(String fulldate) {
        return fulldate.split("-")[0];
    }
}
