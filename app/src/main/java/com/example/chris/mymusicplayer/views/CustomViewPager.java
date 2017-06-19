package com.example.chris.mymusicplayer.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Chris on 6/16/2017.
 */
public class CustomViewPager extends ViewPager {

    private final String TAG = getClass().getSimpleName();

    private GestureDetector mGestureDetector;

    public CustomViewPager(Context context){
        super(context);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
//        setFadingEdgeLength(0);
    }

    public CustomViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());
//        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the y direction
    class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) < Math.abs(distanceX);
        }
    }
}
