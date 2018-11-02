package com.example.general.android.popularmoviesapp.model;

public class VideoTrailer {

    /**
     * Video properties
     */
    private String id;
    private String iso6391Format;
    private String iso31661Format;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;
    /**
     * These variables represent all keys used in json mapping for each video property.
     */
    public static final String ID_KEY = "id";
    public static final String ISO_6391_FORMAT_KEY = "iso_639_1";
    public static final String ISO_3166_FORMAT_KEY = "iso_3166_1";
    public static final String KEY_KEY = "key";
    public static final String NAME_KEY = "name";
    public static final String SITE_KEY = "site";
    public static final String SIZE_KEY = "size";
    public static final String TYPE_KEY = "type";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391Format() {
        return iso6391Format;
    }

    public void setIso6391Format(String iso6391Format) {
        this.iso6391Format = iso6391Format;
    }

    public String getIso31661Format() {
        return iso31661Format;
    }

    public void setIso31661Format(String iso31661Format) {
        this.iso31661Format = iso31661Format;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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
