package com.example.chris.mymusicplayer.services;

/**
 * Created by Chris on 6/16/2017.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.example.chris.mymusicplayer.R;
import com.example.chris.mymusicplayer.activity.MainActivity;
import com.example.chris.mymusicplayer.models.Track;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private ArrayList<Track> trackList;
    private int currentPosition;
    private final IBinder musicBinder = new MusicBinder();
    private String songTitle="";
    //notification id
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private Random rand;

    @Override
    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        currentPosition=0;
        //random
        rand=new Random();
        //create player
        player = new MediaPlayer();
        //initialize
        initMusicPlayer();
    }

    public Track getCurrentTrack(){
        return trackList.get(currentPosition);
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
        PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }


    public void setList(ArrayList<Track> trackList){
        this.trackList = trackList;
    }

    //binder
    public class MusicBinder extends Binder {
    public MusicService getService() {
        return MusicService.this;
    }
}

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    //play a song
    public void playSong(){
        //play
        player.reset();
        //get song
        Track currentTrack = trackList.get(currentPosition);
        //get title
        songTitle = currentTrack.getTitle();
        //get id
        long currSong = currentTrack.getId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        //set the data source
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();



//        ((MainActivity)).setTrackInfo(currentTrack);
    }

    //set the song
    public void setSong(int songIndex){
        currentPosition = songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if playback has reached the end of a track
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        //notification
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play_icon)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
    }

    //playback methods
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void resume(){
        player.start();
    }

    //skip to previous track
    public void playPrev(){
        currentPosition--;
        if(currentPosition<0) currentPosition= trackList.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){
        if(shuffle){
            int newPosition = currentPosition;
            while(newPosition == currentPosition){
                newPosition = rand.nextInt(trackList.size());
            }
            currentPosition = newPosition;
        }
        else{
            currentPosition++;
            if(currentPosition >= trackList.size()) currentPosition = 0;
        }
        playSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    //toggle shuffle
    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

}