package com.studentsins.lust.UI.Fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.studentsins.lust.R;
import com.studentsins.lust.UI.CircleProgressBar;
import com.studentsins.lust.UI.MainActivity;
import com.studentsins.lust.Utils.DoubleClickListener;
import com.studentsins.lust.Utils.ListenerCollection;

/**
 * This class will hold the functionality for the Snapshot fragment
 * Created by koemdzhiev on 07/02/16.
 */
public class SnapshotFragment extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private static String TAG;
    //Circle buttons background images...
    private ImageView mCantDecideBG;
    private ImageView mGoingOutDecideGg;
    private ImageView mTakingItEasyBG;
    //circular custom progress bar for cant decide button
    public static CircleProgressBar mCantDecideProgressBar;
    //circular custom progress bar for going out button
    public static CircleProgressBar mGoingOutProgressBar;
    //circular custom progress bar for "taking it easy" button
    public static CircleProgressBar mTakingItEasyProgressBar;
    //progress animator to handle the animation of the "cant decide" button
    public static ObjectAnimator cantDecideProgressAnimator;
    //progress animator to handle the animation of the "going out" button
    public static ObjectAnimator goingOutProgressAnimator;
    //progress animator to handle the animation of the "taking it easy" button
    public static ObjectAnimator takingItEasyProgressAnimator;

    // Double click listener to perform the circlebar animation on the 3 circle "buttons"
    private DoubleClickListener doubleTapListener = new DoubleClickListener() {
        @Override
        public void onSingleClick(View v) {
            // increment the click count
            doubleTapListener.incrementCount();
            // if the click count is 2 show the user a message what she/he is supposed to do...
            if(doubleTapListener.getClickCounter() == 2){
                Snackbar.make(mGoingOutDecideGg,"Double tap to select (:", Snackbar.LENGTH_SHORT).show();
                // reset the counter...
                doubleTapListener.setClickCounter(0);
            }

        }
        @Override
        public void onDoubleClick(View v) {
            // reset tap counter
            doubleTapListener.setClickCounter(0);
            // switch the id of the v...
            switch (v.getId()){
                // the v id is going out button
                case R.id.going_out_black_circle_bg:
                    //if the user pressed the "going out" button...
                    //check if the other two progress bar animators are NOT currently animating...
                    if(!(cantDecideProgressAnimator.isRunning() || takingItEasyProgressAnimator.isRunning())) {
                        SnapshotFragment.mTakingItEasyProgressBar.setProgress(0);
                        SnapshotFragment.mCantDecideProgressBar.setProgress(0);
                        //start the cant decide progress bar animation...
                        SnapshotFragment.goingOutProgressAnimator.start();
                        Log.d(TAG, "ACTION_DOWN - Going out - executed");
                    }
                    break;
                // the v id is cant decide button
                case R.id.cant_decide_black_circle_bg:
                    //if the user pressed the "cant decide" button...
                    //check if the other two progress bar animators are NOT currently animating...
                    if(!(goingOutProgressAnimator.isRunning() || takingItEasyProgressAnimator.isRunning())) {
                        SnapshotFragment.mGoingOutProgressBar.setProgress(0);
                        SnapshotFragment.mTakingItEasyProgressBar.setProgress(0);
                        //start the cant decide progress bar animation...
                        SnapshotFragment.cantDecideProgressAnimator.start();
                        Log.d(TAG, "ACTION_DOWN - Cant Decide - executed");
                    }
                    break;
                // the v id is taking it easy button
                case R.id.taking_it_easy_black_circle_bg:
                    //if the user pressed the taking it easy" button...
                    //check if the other two progress bar animators are NOT currently animating...
                    if(!(goingOutProgressAnimator.isRunning() || cantDecideProgressAnimator.isRunning())) {
                        SnapshotFragment.mCantDecideProgressBar.setProgress(0);
                        SnapshotFragment.mGoingOutProgressBar.setProgress(0);
                        //start the cant decide progress bar animation...
                        SnapshotFragment.takingItEasyProgressAnimator.start();
                        Log.d(TAG, "ACTION_DOWN - taking it easy - executed");
                    }
                    break;
            }

        }
    };


/*
 *   Method that returns a new instance of
 *   the fragment - recommended way of creating a fragment
 */
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snapshot_layout,container,false);
        mCantDecideBG = (ImageView)view.findViewById(R.id.cant_decide_black_circle_bg);
        //set up the cant decide circle image for touches...
        mCantDecideBG.setOnClickListener(doubleTapListener);

        mGoingOutDecideGg = (ImageView)view.findViewById(R.id.going_out_black_circle_bg);
        //set up the going out circle image for touches...
        mGoingOutDecideGg.setOnClickListener(doubleTapListener);

        mTakingItEasyBG = (ImageView)view.findViewById(R.id.taking_it_easy_black_circle_bg);
        mTakingItEasyBG.setOnClickListener(doubleTapListener);

        mGoingOutProgressBar = (CircleProgressBar)view.findViewById(R.id.goingOutProgressBar);
        //set up the project animator to animate the "going out" progress bar from 0 to 100
        goingOutProgressAnimator = ObjectAnimator.ofFloat(mGoingOutProgressBar, "progress", 0.0f, 100.0f);
        //add the animation listener to the progress animator to check when the progress has started,finished...
        goingOutProgressAnimator.addListener(ListenerCollection.goingOutProgressBarAnimationListener);
        goingOutProgressAnimator.setDuration(1200);


        mCantDecideProgressBar = (CircleProgressBar)view.findViewById(R.id.cantDecideProgressBar);
        //set up the project animator to animate the "cant decide" progress bar from 0 to 100
        cantDecideProgressAnimator = ObjectAnimator.ofFloat(mCantDecideProgressBar, "progress", 0.0f, 100.0f);
        //add the animation listener to the progress animator to check when the progress has started,finished...
        cantDecideProgressAnimator.addListener(ListenerCollection.cantDecideProgressBarAnimationListener);
        //set the duration of the animation to 1.2 seconds
        cantDecideProgressAnimator.setDuration(1200);


        mTakingItEasyProgressBar = (CircleProgressBar)view.findViewById(R.id.takingItEasyProgressBar);
        //set up the project animator to animate the "taking it easy" progress bar from 0 to 100
        takingItEasyProgressAnimator = ObjectAnimator.ofFloat(mTakingItEasyProgressBar, "progress", 0.0f, 100.0f);
        //add the animation listener to the progress animator to check when the progress has started,finished...
        takingItEasyProgressAnimator.addListener(ListenerCollection.takingItEasyProgressBarAnimationListener);
        //set the duration of the animation to 1.2 seconds
        takingItEasyProgressAnimator.setDuration(1200);

        view.findViewById(R.id.relativeLayoutMainBG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).mFloatingActionsMenu.collapse();
            }
        });

        Log.d("MainActivity", "onCreateView");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Snapshot fragment", "onDestroyView");
    }

}
