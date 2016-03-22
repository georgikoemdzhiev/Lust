package com.studentsins.lust.Utils;

import android.view.View;
import android.widget.Toast;

/**
 * Custom onClickListener that implements "Double tap" functionality
 * Created by Georgi on 3/22/2016.
 */
public abstract class DoubleClickListener implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

    long lastClickTime = 0;
    int tapCount = 0;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            tapCount = 0;
            onDoubleClick(v);
        } else {
            if(tapCount >= 1) {
                Toast.makeText(v.getContext(), "Double tap to select :)", Toast.LENGTH_SHORT).show();
            }
            onSingleClick(v);
            tapCount++;
        }
       // tapCount = 0;
        lastClickTime = clickTime;
    }

    public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);
}