package com.studentsins.lust.Utils;

import android.view.View;

/**
 * Custom onClickListener that implements "Double tap" functionality
 * Created by Georgi on 3/22/2016.
 */
public abstract class DoubleClickListener implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

    long lastClickTime = 0;
    int clickCounter = 0;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            onDoubleClick(v);
        } else {
            onSingleClick(v);
        }
        lastClickTime = clickTime;

    }

    public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);

    public int getClickCounter() {
        return clickCounter;
    }

    public void setClickCounter(int clickCounter) {
        this.clickCounter = clickCounter;
    }

    public void incrementCount(){
        this.clickCounter++;
    }
}