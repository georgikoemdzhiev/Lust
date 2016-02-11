package com.studentsins.lust.Utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.studentsins.lust.UI.MainActivity;

/**
 * Created by koemdzhiev on 11/02/16.
 */
public class ListenerCollection {
    private static final String TAG = ListenerCollection.class.getSimpleName();
    private static boolean isFabShown = true;

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

    private static void hideFAB() {
        MainActivity.mFloatingActionsMenu.animate()
                .setDuration(150)
                .translationY(300);
        isFabShown = false;
    }

    public static void showFAB() {
        MainActivity.mFloatingActionsMenu.animate()
                .setDuration(200)
                .translationY(0);
        isFabShown = true;
    }
}
