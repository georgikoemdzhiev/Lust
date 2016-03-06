package com.studentsins.lust.Utils;

import android.animation.Animator;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.studentsins.lust.UI.MainActivity;
import com.studentsins.lust.UI.SnapshotFragment;

/**
 * Class to hold any listeners (whenever possible) needed elsewhere in the app.
 * The purpose of doing this is to make the code spread in different classes and thus more readable.
 * Created by koemdzhiev on 11/02/16.
 */
public class ListenerCollection {
    private static final String TAG = ListenerCollection.class.getSimpleName();
    private static boolean isFabShown = true;
//Listener to handle the showing/hiding of the floating action button - FAB
    public  static RecyclerView.OnScrollListener showHideFAB = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 && isFabShown) {
                // Scrolling up
                Log.d(TAG, "Scrolling UP - RecyclerLayout");
                hideFAB();
            } else if(dy < 0 && !isFabShown){
                // Scrolling down
                Log.d(TAG, "Scrolling DOWN - RecyclerLayout");
                showFAB();

            }
        }
    };

    public static ViewPager.OnPageChangeListener onPageChangeListenerShowFAB = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {}
        @Override
        public void onPageScrollStateChanged(int state) {


            showFAB();
            //show the toolbar

            //expand the toolbar... NOW COMMENTED DUE TO A GOOGLE BUG
//            expandToolbar();
//            Log.d(TAG,"onPageScrollStateChanded");
        }
    };
//Helper method to hide the FAB...
    private static void hideFAB() {
        MainActivity.mFloatingActionsMenu.animate()
                .setDuration(150)
                .translationY(300);
        isFabShown = false;
    }
//Helper method to show the FAB...
    public static void showFAB() {
        MainActivity.mFloatingActionsMenu.animate()
                .setDuration(200)
                .translationY(0);
        isFabShown = true;
    }
//    Method to expand the toolbar. This is not used since it is not needed at the moment.
    public static void expandToolbar(){
        //setExpanded(boolean expanded, boolean animate)
        MainActivity.appBarLayout.setExpanded(true, true);
    }
//progress bar animation listener to handle the progress bar animations in Snapshot fragment
    public static Animator.AnimatorListener progressBarAnimationListener =  new  Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            SnapshotFragment.mCantDecideProgressBar.setProgress(0);
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            // Log.d(TAG, "onAnimationEnd");
            if(SnapshotFragment.mCantDecideProgressBar.getProgress() < 100){
                SnapshotFragment.mCantDecideProgressBar.setProgress(0);
            }else {
                SnapshotFragment.mCantDecideProgressBar.setProgress(100);
            }
            //mNumSins.setText(numberOfSins+"");
        }
        @Override
        public void onAnimationCancel(Animator animator) {}
        @Override
        public void onAnimationRepeat(Animator animator) {}
    };
//On touch listener to handle the going out, cant decide and staying in circle touches
    public static View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                SnapshotFragment.cantDecideProgressAnimator.cancel();
                Log.d(TAG, "ACTION_UP canceled");
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                SnapshotFragment.cantDecideProgressAnimator.start();
                Log.d(TAG, "ACTION_DOWN executed");
            }
            return true;
        }
    };
}