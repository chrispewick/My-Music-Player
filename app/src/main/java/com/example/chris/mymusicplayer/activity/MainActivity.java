package com.example.chris.mymusicplayer.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chris.mymusicplayer.R;
import com.example.chris.mymusicplayer.models.Track;
import com.example.chris.mymusicplayer.musiclibrarycontainer.MusicLibraryContainer;
import com.example.chris.mymusicplayer.services.MusicService;
import com.example.chris.mymusicplayer.views.FullDrawerLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    private final String TAG = getClass().getSimpleName();

    private Fragment currentContainer;

    private ImageView navigationDrawerIcon;
    private FullDrawerLayout navigationDrawer;
    private LinearLayout navigationLayout;
    private ListView navigationListView;
    private String[] navigationList;

    private LinearLayout bottomTaskBar;
    private RelativeLayout albumArtworkLayout;
    private ImageView albumArtworkIcon;
    private RelativeLayout trackInfoLayout;
    private TextView curTrackTitle;
    private TextView curTrackAlbumArtist;
    private RelativeLayout playPauseLayout;
    private ImageView playPauseIcon;
    private ImageView playPrev;
    private ImageView playNext;

    private ArrayList<Track> trackList;
    private int currentIndexTrackList;


    //service
    private MusicService musicService;
    private Intent playIntent;

    //binding
    private boolean musicBound = false;

    //controller
    private MediaController controller;

    //activity and playback pause flags
    private boolean paused=false, playbackPaused=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Can add more as per requirement
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
            //TODO: Maybe open fragment/screen/dialog(then stay on a splash screen) informing the user that they must grant the permission
        } else {
            trackList = new ArrayList<>();
            openDefaultScreen();
        }

        this.configureControlBar();
        this.configureSideDrawer();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    private void configureSideDrawer(){
        navigationDrawer = (FullDrawerLayout) findViewById(R.id.navigation_drawer);
        navigationListView = (ListView) findViewById(R.id.navigation_list);
        navigationList = new String[]{"Music Library","Playlists"};
//        navigationListView.setAdapter(new ArrayAdapter<String>(this,
//                R.id.navigation_option,
//                navigationList));
        navigationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Open the container, highlight the selection, set action bar title
                Log.i(TAG, "Clicked: "+navigationList[position]);
            }
        });

        navigationDrawerIcon = (ImageView) findViewById(R.id.side_drawer_icon);
        navigationDrawerIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //In case the user was typing, close the keyboard
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                navigationDrawer.openDrawer(findViewById(R.id.navigation_drawer_layout));
            }
        });
    }

    private void configureControlBar(){
        curTrackTitle = (TextView) findViewById(R.id.track_name);
        curTrackAlbumArtist = (TextView) findViewById(R.id.album_artist);

        playPauseIcon = (ImageView) findViewById(R.id.play_pause_icon);
        playPrev = (ImageView) findViewById(R.id.play_prev_icon);
        playNext = (ImageView) findViewById(R.id.play_next_icon);

        playPauseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trackList != null && trackList.size() > 0) {
                    if (isPlaying()) {
                        //Pause
                        playPauseIcon.setImageResource(R.drawable.play_icon);
                        musicService.pausePlayer();
                    } else {
                        //Play/Resume
                        playPauseIcon.setImageResource(R.drawable.pause_icon);
                        musicService.resume();
                    }
                    setTrackInfo(musicService.getCurrentTrack());
                }
            }
        });

        playPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Play prev
                if(trackList != null && trackList.size() > 0) {
                    musicService.playPrev();
                    setTrackInfo(musicService.getCurrentTrack());
                    playPauseIcon.setImageResource(R.drawable.pause_icon);
                }
            }
        });

        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Play Next
                if(trackList != null && trackList.size() > 0) {
                    musicService.playNext();
                    setTrackInfo(musicService.getCurrentTrack());
                    playPauseIcon.setImageResource(R.drawable.pause_icon);
                }
            }
        });
    }

    public void setTrackInfo(Track track){
        curTrackTitle.setText(track.getTitle());
        String curTrackAlbumArtistStr = track.getAlbumTitle() +" | "+ track.getArtist();
        curTrackAlbumArtist.setText(curTrackAlbumArtistStr);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            trackList = new ArrayList<>();
            openDefaultScreen();
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList((ArrayList<Track>) trackList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    /**
     * Opens the initial fragment the user will first see when opening the app. Set to the home
     * screen by default, but the user can change this to another fragment.
     */
    private void openDefaultScreen(){
        //TODO: open based on user's settings
        //TODO: instead of default fragment, have the user select a default ViewPager and a default index in that viewpager
        FragmentTransaction containerTransaction = getSupportFragmentManager().beginTransaction();
        MusicLibraryContainer fragment = new MusicLibraryContainer();
        currentContainer = fragment;

        String userSetDefaultFragmentName = "DefaultFragment";
        containerTransaction.add(R.id.frame_layout, fragment, userSetDefaultFragmentName);
//        containerTransaction.addToBackStack("HomeFragment");
        containerTransaction.commit();
    }

    //start and bind the service when the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            //TODO: can leak here!
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void playTrack(Track track){
        if(trackList == null){
            trackList = new ArrayList<>();
        }
        trackList.add(track);
        musicService.setList(trackList);
        musicService.setSong(trackList.indexOf(track));
        musicService.playSong();
        if(playbackPaused){
            setController();
            playbackPaused = false;
        }

        playPauseIcon.setImageResource(R.drawable.pause_icon);
        setTrackInfo(track);
//        controller.show(0);
    }

    //user song select
    public void songPicked(View view){
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.playSong();
        if(playbackPaused){
            setController();
            playbackPaused = false;
        }
//        controller.show(0);
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicService!=null && musicBound && musicService.isPng())
            return musicService.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if(musicService!=null && musicBound && musicService.isPng())
            return musicService.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicService!=null && musicBound)
            return musicService.isPng();
        return false;
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicService.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }

    @Override
    public void start() {
        musicService.resume();
    }

    //set the controller up
    private void setController(){
        controller = new MediaController(this);
        //set previous and next button listeners
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        //set and show
        controller.setMediaPlayer(this);
//        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }

    private void playNext(){
        musicService.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
//        controller.show(0);
    }

    private void playPrev(){
        musicService.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
//        controller.show(0);
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
//        controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService=null;
        super.onDestroy();
    }
}
