package com.example.chris.mymusicplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Chris on 6/16/2017.
 */
public class CustomListView extends ListView{
    private final String TAG = getClass().getSimpleName();

    private GestureDetector mGestureDetector;

    public CustomListView(Context context){
        super(context);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
//        setFadingEdgeLength(0);
    }

    public CustomListView(Context context, AttributeSet attrs){
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
//        setFadingEdgeLength(0);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
//        setFadingEdgeLength(0);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}
