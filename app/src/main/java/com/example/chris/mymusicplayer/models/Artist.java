package com.example.chris.mymusicplayer.models;

import java.util.ArrayList;

/**
 * Created by Chris on 6/15/2017.
 */
public class Artist implements IMusicObject{
    private final String TAG = getClass().getSimpleName();

    private long id;
    private String name;
    private String numberAlbums;
    private String numberTracks;

    public Artist(long id, String name, String numberAlbums, String numberTracks){
        this.id = id;
        this.name = name;
        this.numberAlbums = numberAlbums;
        this.numberTracks = numberTracks;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumberAlbums() {
        return numberAlbums;
    }

    public String getNumberTracks() {
        return numberTracks;
    }
}
