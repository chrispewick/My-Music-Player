package com.example.chris.mymusicplayer.musiclibrarycontainer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chris.mymusicplayer.R;
import com.example.chris.mymusicplayer.activity.MainActivity;
import com.example.chris.mymusicplayer.adapters.MusicListAdapter;
import com.example.chris.mymusicplayer.models.IMusicObject;
import com.example.chris.mymusicplayer.models.Track;

import java.util.ArrayList;

/**
 * Created by Chris on 6/15/2017.
 */
public class SongListFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private MusicListAdapter adapter;

    private ArrayList<IMusicObject> trackList;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        trackList = new ArrayList<>();
        this.getTrackList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_default_home, container, false);

        listView = (ListView) view.findViewById(R.id.home_list_view);
//        trackList = new ArrayList<>();
//        this.getTrackList();

        adapter = new MusicListAdapter(this.getActivity(), trackList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "Track click: "+((Track)trackList.get(i)).getFilePath());
//                ((MainActivity) getActivity()).playSelectedTrack((Track)trackList.get(i));
                ((MainActivity) getActivity()).playTrack((Track)trackList.get(i));
            }
        });

        return view;
    }

    private void getTrackList(){
        Log.i(TAG, "getTrackList()");
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,
                null,
                null,
                null,
                MediaStore.Audio.Media.ARTIST + " ASC");

        if(musicCursor!=null && musicCursor.moveToFirst()) {
            //add songs to list
            do {
                long id = musicCursor.getLong(musicCursor.getColumnIndex
                        (MediaStore.Audio.Media._ID));
                String title = musicCursor.getString(musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.TITLE));
                String artist = musicCursor.getString(musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ARTIST));
                String albumTitle = musicCursor.getString(musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM));
                String filePath = musicCursor.getString(musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.DATA));
                long albumID = musicCursor.getLong(musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID));
                //Use the albumID to get the artwork path
//                String artworkPath = getAlbumArtworkString(albumID);

//                trackList.add(new Track(id, title, artist, albumTitle, filePath, artworkPath));
                trackList.add(new Track(id, albumID, title, artist, albumTitle, filePath));
            }
            while (musicCursor.moveToNext());
//            musicCursor.close();
        }

//        for(IMusicObject track : trackList){
//            ((Track) track).setArtworkPath(getAlbumArtworkString(((Track) track).getAlbumId()));
//        }
        Log.i(TAG, "TrackList size: "+trackList.size());
    }

    private String getAlbumArtworkString(long albumId){
        ContentResolver musicResolver = getActivity().getContentResolver();
        String[] mProjection = new String[]{
                MediaStore.Audio.Albums.ALBUM_ART
        };

        Cursor albumCursor = musicResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                mProjection,
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {String.valueOf(albumId)},
                null);


        if (albumCursor != null && albumCursor.moveToFirst()) {
            String path = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//            albumCursor.close();
            return path;
        } else {
            return null;
        }
    }
}
