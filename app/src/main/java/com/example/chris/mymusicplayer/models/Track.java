package com.example.chris.mymusicplayer.models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.media.session.IMediaControllerCallback;

/**
 * Created by Chris on 6/14/2017.
 */
public class Track implements IMusicObject{
    private final String TAG = getClass().getSimpleName();

    private long id;
    private long albumId;
    private String title;
    private String artist;
    private String albumTitle;
    private String filePath;
    private String artworkPath;

    public Track(long songID, long albumID, String songTitle, String songArtist, String albumTitle, String filePath) {
        this.id = songID;
        this.albumId = albumID;
        this.title = songTitle;
        this.artist = songArtist;
        this.albumTitle = albumTitle;
        this.filePath = filePath;
//        this.artworkPath = artworkPath;
    }

    public long getId() {
        return id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setArtworkPath(String artworkPath) {
        this.artworkPath = artworkPath;
    }

    public String getArtworkPath() {
        return artworkPath;
    }

    public Bitmap getArtwork(){
        return BitmapFactory.decodeFile(artworkPath);
    }


}
