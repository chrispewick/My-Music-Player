package com.example.chris.mymusicplayer.musiclibrarycontainer;

import android.content.ContentResolver;
import android.database.Cursor;
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
import com.example.chris.mymusicplayer.adapters.MusicListAdapter;
import com.example.chris.mymusicplayer.models.Album;
import com.example.chris.mymusicplayer.models.IMusicObject;

import java.util.ArrayList;

/**
 * Created by Chris on 6/15/2017.
 */
public class AlbumListFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private MusicListAdapter adapter;

    private ArrayList<IMusicObject> albumList;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default_home, container, false);

        listView = (ListView) view.findViewById(R.id.home_list_view);
        albumList = new ArrayList<>();
        this.getAlbumList();

        adapter = new MusicListAdapter(this.getActivity(), albumList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "Album click: "+((Album) albumList.get(i)).getTitle());
            }
        });

        return view;
    }

    private void getAlbumList(){
        Log.i(TAG, "getAlbumList()");
        ContentResolver musicResolver = getActivity().getContentResolver();

        String[] mProjection = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.LAST_YEAR,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        Cursor artistCursor = musicResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                mProjection,
                null,
                null,
                MediaStore.Audio.Albums.ALBUM + " ASC");

        if(artistCursor!=null && artistCursor.moveToFirst()) {
            //add songs to list
            do {
                long id = artistCursor.getLong(artistCursor.getColumnIndex
                        (MediaStore.Audio.Albums._ID));
                String albumTitle = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Albums.ALBUM));
                String artist = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Albums.ARTIST));
                String numberTracks = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                String year = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Albums.LAST_YEAR));
                String artworkPath = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Albums.ALBUM_ART));

                albumList.add(new Album(id, albumTitle, artist, numberTracks, year, artworkPath));
            } while (artistCursor.moveToNext());
        }
    }
}