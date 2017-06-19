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
import com.example.chris.mymusicplayer.models.Artist;
import com.example.chris.mymusicplayer.models.IMusicObject;

import java.util.ArrayList;

/**
 * Created by Chris on 6/15/2017.
 */
public class ArtistListFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private MusicListAdapter adapter;

    private ArrayList<IMusicObject> artistList;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default_home, container, false);

        listView = (ListView) view.findViewById(R.id.home_list_view);
        artistList = new ArrayList<>();
        this.getArtistList();

        adapter = new MusicListAdapter(this.getActivity(), artistList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "Track click: "+((Artist) artistList.get(i)).getName());
            }
        });

        return view;
    }

    private void getArtistList(){
        Log.i(TAG, "getArtistList()");
        ContentResolver musicResolver = getActivity().getContentResolver();

        String[] mProjection = new String[]{
                        MediaStore.Audio.Artists._ID,
                        MediaStore.Audio.Artists.ARTIST,
                        MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
                };

        Cursor artistCursor = musicResolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                mProjection,
                null,
                null,
                MediaStore.Audio.Artists.ARTIST + " ASC");

        if(artistCursor!=null && artistCursor.moveToFirst()) {
            //add songs to list
            do {
                long id = artistCursor.getLong(artistCursor.getColumnIndex
                        (MediaStore.Audio.Artists._ID));
                String artist = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Artists.ARTIST));
                String numberTracks = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                String numberAlbums = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));

                artistList.add(new Artist(id, artist, numberAlbums, numberTracks));
            } while (artistCursor.moveToNext());
        }
    }
}