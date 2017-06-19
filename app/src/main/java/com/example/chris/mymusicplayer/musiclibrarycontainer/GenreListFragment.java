package com.example.chris.mymusicplayer.musiclibrarycontainer;

import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chris.mymusicplayer.R;
import com.example.chris.mymusicplayer.adapters.MusicListAdapter;
import com.example.chris.mymusicplayer.models.Genre;
import com.example.chris.mymusicplayer.models.IMusicObject;

import java.util.ArrayList;

/**
 * Created by Chris on 6/15/2017.
 */
public class GenreListFragment extends Fragment{
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
                Log.i(TAG, "Album click: "+((Genre) albumList.get(i)).getName());
            }
        });

        return view;
    }

    private void getAlbumList(){
        Log.i(TAG, "getAlbumList()");
        ContentResolver musicResolver = getActivity().getContentResolver();

        String[] mProjection = new String[]{
                MediaStore.Audio.Genres._ID,
                MediaStore.Audio.Genres.NAME
        };

        Cursor artistCursor = musicResolver.query(
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                mProjection,
                null,
                null,
                MediaStore.Audio.Genres.NAME + " ASC");

        if(artistCursor!=null && artistCursor.moveToFirst()) {
            //add songs to list
            do {
                long id = artistCursor.getLong(artistCursor.getColumnIndex
                        (MediaStore.Audio.Albums._ID));
                String name = artistCursor.getString(artistCursor.getColumnIndex
                        (MediaStore.Audio.Genres.NAME));

                albumList.add(new Genre(id, name));
            } while (artistCursor.moveToNext());
        }
    }
}
