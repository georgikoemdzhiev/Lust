package com.studentsins.lust.UI;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.studentsins.lust.R;

/**
 * Created by koemdzhiev on 07/02/16.
 */
public class SnapshotFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static String TAG;
    private int mPage;
    private ImageView mCantDecideGg;
    private CircleProgressBar mCantDecideProgressBar;
    private ObjectAnimator cantDecideProgressAnimator;
    private Animator.AnimatorListener progressBarAnimationListener =  new  Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            mCantDecideProgressBar.setProgress(0);
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            // Log.d(TAG, "onAnimationEnd");
            mCantDecideProgressBar.setProgress(100);
            //mNumSins.setText(numberOfSins+"");
        }
        @Override
        public void onAnimationCancel(Animator animator) {}
        @Override
        public void onAnimationRepeat(Animator animator) {}
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                cantDecideProgressAnimator.cancel();
                Log.d(TAG, "ACTION_UP canceled");
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                cantDecideProgressAnimator.start();
                Log.d(TAG, "ACTION_DOWN executed");
            }
            return false;
        }
    };

    public static SnapshotFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SnapshotFragment fragment = new SnapshotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        TAG = getActivity().getClass().getSimpleName();
        Log.d("MainActivity", "onCreate" + mPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snapshot_layout,container,false);
        mCantDecideGg = (ImageView)view.findViewById(R.id.cant_devide_black_circle_bg);

        mCantDecideGg.setOnTouchListener(onTouchListener);
        mCantDecideProgressBar = (CircleProgressBar)view.findViewById(R.id.cantDecideProgressBar);
        //set up the project animator to animate the progress bar from 0 to 100
        cantDecideProgressAnimator = ObjectAnimator.ofFloat(mCantDecideProgressBar, "progress", 0.0f, 100.0f);
        //add the animation listener to the progress animator to check when the progress has started,finished...
        cantDecideProgressAnimator.addListener(progressBarAnimationListener);
        //set the duration of the animation to 1.2 seconds
        cantDecideProgressAnimator.setDuration(1200);
        Log.d("MainActivity", "onCreateView" + mPage);
        return view;
    }
}
