package com.example.general.android.popularmoviesapp.model;

/**
 * The class that represents the information about the trailer of a Movie
 */
public class VideoTrailer {

    /**
     * Video properties
     */
    private String id;
    private String key;
    private String name;
    private int size;
    private String type;
    /**
     * These variables represent all keys used in json mapping for each video property.
     */
    public static final String ID_KEY = "id";
    public static final String KEY_KEY = "key";
    public static final String NAME_KEY = "name";
    public static final String SIZE_KEY = "size";
    public static final String TYPE_KEY = "type";


    public VideoTrailer() {
        id = "";
        key = "";
        name = "";
        size = 0;
        type = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
