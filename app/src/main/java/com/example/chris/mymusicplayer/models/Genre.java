package com.example.chris.mymusicplayer.models;

/**
 * Created by Chris on 6/16/2017.
 */
public class Genre implements IMusicObject{
    private final String TAG = getClass().getSimpleName();

    private long id;
    private String name;

    public Genre(long id, String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
