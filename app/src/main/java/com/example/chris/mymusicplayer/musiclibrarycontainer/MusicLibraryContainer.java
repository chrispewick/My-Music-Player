package com.example.chris.mymusicplayer.musiclibrarycontainer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.mymusicplayer.R;
import com.example.chris.mymusicplayer.adapters.MusicLibraryPagerAdapter;

/**
 * Created by Chris on 6/15/2017.
 */
public class MusicLibraryContainer extends Fragment{
    private final String TAG = getClass().getSimpleName();

    private ViewPager viewPager;
    private MusicLibraryPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.music_library_viewpager);
        pagerAdapter = new MusicLibraryPagerAdapter(getContext(),getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setUpViewPager(){

    }
}
