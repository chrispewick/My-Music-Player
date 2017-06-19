package com.example.chris.mymusicplayer.models;

/**
 * Created by Chris on 6/15/2017.
 */
public class Album implements IMusicObject{
    private final String TAG = getClass().getSimpleName();

    private long id;
    private String title;
    private String artist;
    private String numberTracks;
    private String year;
    private String artworkPath;

    public Album(long id, String title, String artist, String numberTracks, String year, String artworkPath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.numberTracks = numberTracks;
        this.year = year;
        this.artworkPath = artworkPath;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getNumberTracks() {
        return numberTracks;
    }

    public String getYear() {
        return year;
    }

    public String getArtworkPath() {
        return artworkPath;
    }
}
