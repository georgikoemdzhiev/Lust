package com.studentsins.lust.Utils;

import android.animation.Animator;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.studentsins.lust.R;
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
    public  static RecyclerView.OnScrollListener showHideCollapseFAB = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //collapse the floating action button when scrolling to make sure it is cosed before hiding it...
            MainActivity.mFloatingActionsMenu.collapse();
            //hide the floating action button...
            if (dy > 0 && isFabShown) {
                // Scrolling up
                Log.d(TAG, "Scrolling UP - RecyclerLayout");
                hideFAB();
            } else if(dy < 0 && !isFabShown){
                // Scrolling down
                //show the floating action button...
                Log.d(TAG, "Scrolling DOWN - RecyclerLayout");
                showFAB();

            }
        }
    };

    public static ViewPager.OnPageChangeListener onPageChangeListenerShowFAB = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected...");
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            showFAB();
            MainActivity.mFloatingActionsMenu.collapse();
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



//cantDecideProgressBarAnimationListener to handle the progress bar animations in Snapshot fragment
    public static Animator.AnimatorListener cantDecideProgressBarAnimationListener =  new  Animator.AnimatorListener() {
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

    public static Animator.AnimatorListener takingItEasyProgressBarAnimationListener = new  Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            SnapshotFragment.mTakingItEasyProgressBar.setProgress(0);
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            // Log.d(TAG, "onAnimationEnd");
            if(SnapshotFragment.mTakingItEasyProgressBar.getProgress() < 100){
                SnapshotFragment.mTakingItEasyProgressBar.setProgress(0);
            }else {
                SnapshotFragment.mTakingItEasyProgressBar.setProgress(100);
            }
            //mNumSins.setText(numberOfSins+"");
        }
        @Override
        public void onAnimationCancel(Animator animator) {}
        @Override
        public void onAnimationRepeat(Animator animator) {}
    };;

    //goingOutProgressBarAnimationListener to handle the progress bar animations in Snapshot fragment
    public static Animator.AnimatorListener goingOutProgressBarAnimationListener =  new  Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            SnapshotFragment.mGoingOutProgressBar.setProgress(0);
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            // Log.d(TAG, "onAnimationEnd");
            if(SnapshotFragment.mGoingOutProgressBar.getProgress() < 100){
                SnapshotFragment.mGoingOutProgressBar.setProgress(0);
            }else {
                SnapshotFragment.mGoingOutProgressBar.setProgress(100);
            }
            //mNumSins.setText(numberOfSins+"");
        }
        @Override
        public void onAnimationCancel(Animator animator) {}
        @Override
        public void onAnimationRepeat(Animator animator) {}
    };
}