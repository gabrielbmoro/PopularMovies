package com.example.general.android.popularmoviesapp.model;

/**
 * This class represents the review about some Movie
 */
public class Review {

    private String author;
    private String content;
    private String id;

    public static final String AUTHOR_KEY = "author";
    public static final String CONTENT_KEY = "content";
    public static final String ID_KEY = "id";

    public Review() {
        author = "";
        content = "";
        id = "";
    }

    /**
     * Review's properties
     */
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
