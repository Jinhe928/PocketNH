package com.pocketnhs.pocketnhsandroid;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by MacBook Pro on 26.8.2016..
 */
public class LiveWellActivity extends SingleFragmentActivity implements  GestureDetector.OnGestureListener {

    private GestureDetectorCompat mGestureDetector;


    @Override
    protected Fragment createFragment() {
        return LiveWellFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGestureDetector();
    }




    private void setupGestureDetector() {
        mGestureDetector = new GestureDetectorCompat(this, this);

    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = super.dispatchTouchEvent(ev);
        handled = mGestureDetector.onTouchEvent(ev);
        return handled;
    }




    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        double SWIPE_MIN_DISTANCE = 100;
        double SWIPE_THRESHOLD_VELOCITY = 500;
        double diff = e2.getX() - e1.getX();
        if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                && Math.abs(velocityY) < Math.abs(velocityX)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }else {
                finish();
            }
        }
        return false;
    }

}
