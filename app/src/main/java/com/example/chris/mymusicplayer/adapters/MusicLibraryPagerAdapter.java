package com.example.chris.mymusicplayer.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.chris.mymusicplayer.R;
import com.example.chris.mymusicplayer.musiclibrarycontainer.AlbumListFragment;
import com.example.chris.mymusicplayer.musiclibrarycontainer.ArtistListFragment;
import com.example.chris.mymusicplayer.musiclibrarycontainer.GenreListFragment;
import com.example.chris.mymusicplayer.musiclibrarycontainer.SongListFragment;

/**
 * Created by Chris on 6/15/2017.
 */
public class MusicLibraryPagerAdapter extends FragmentPagerAdapter {
    private final String TAG = getClass().getSimpleName();
    private Fragment currentFragment;

    private ArtistListFragment artistListFragment;
    private AlbumListFragment albumListFragment;
    private SongListFragment songListFragment;
    private GenreListFragment genreListFragment;

    private String tabTitles[];

    public MusicLibraryPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        tabTitles = new String[] {context.getResources().getString(R.string.artists),
                context.getResources().getString(R.string.albums),
                context.getResources().getString(R.string.songs),
                context.getResources().getString(R.string.genres)};

        artistListFragment = new ArtistListFragment();
        albumListFragment = new AlbumListFragment();
        songListFragment = new SongListFragment();
        genreListFragment = new GenreListFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
//        Log.i(TAG, "getItem()");

        if(position == 0){
            return artistListFragment;
        } else if(position == 1){
            return albumListFragment;
        } else if(position == 2){
            return songListFragment;
        } else {
            return genreListFragment;
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
//        Log.i(TAG, "setPrimaryItem()");
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        Log.i(TAG, "getPageTitle()");
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        Log.d(TAG, "destroy!");
    }

    public Fragment getCurrentFragment(){
//        Log.i(TAG, "getCurrentFragment()");
        return currentFragment;
    }
}