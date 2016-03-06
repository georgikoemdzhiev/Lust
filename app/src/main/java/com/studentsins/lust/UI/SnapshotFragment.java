package com.studentsins.lust.UI;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.studentsins.lust.R;
import com.studentsins.lust.Utils.ListenerCollection;

/**
 * This class will hold the functionality for the Snapshot fragment
 * Created by koemdzhiev on 07/02/16.
 */
public class SnapshotFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private static String TAG;
    private ImageView mCantDecideGg;
    private ImageView mGoingOutDecideGg;
    public static CircleProgressBar mCantDecideProgressBar;
    public static CircleProgressBar mGoingOutProgressBar;
    public static ObjectAnimator cantDecideProgressAnimator;
    public static ObjectAnimator goingOutProgressAnimator;


// Method that returns a new instance of the fragment - recommended way of creating a fragment
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
        mCantDecideGg = (ImageView)view.findViewById(R.id.cant_decide_black_circle_bg);
        //set up the cant decide circle image for touches...
        mCantDecideGg.setOnTouchListener(ListenerCollection.onTouchListener);

        mGoingOutDecideGg = (ImageView)view.findViewById(R.id.going_out_black_circle_bg);
        //set up the going out circle image for touches...
        mGoingOutDecideGg.setOnTouchListener(ListenerCollection.onTouchListener);

        mGoingOutProgressBar = (CircleProgressBar)view.findViewById(R.id.goingOutProgressBar);
        //set up the project animator to animate the going out progress bar from 0 to 100
        goingOutProgressAnimator = ObjectAnimator.ofFloat(mGoingOutProgressBar, "progress", 0.0f, 100.0f);
        //add the animation listener to the progress animator to check when the progress has started,finished...
        goingOutProgressAnimator.addListener(ListenerCollection.goingOutProgressBarAnimationListener);
        goingOutProgressAnimator.setDuration(1200);


        mCantDecideProgressBar = (CircleProgressBar)view.findViewById(R.id.cantDecideProgressBar);
        //set up the project animator to animate the cant decide progress bar from 0 to 100
        cantDecideProgressAnimator = ObjectAnimator.ofFloat(mCantDecideProgressBar, "progress", 0.0f, 100.0f);
        //add the animation listener to the progress animator to check when the progress has started,finished...
        cantDecideProgressAnimator.addListener(ListenerCollection.cantDecideProgressBarAnimationListener);
        //set the duration of the animation to 1.2 seconds
        cantDecideProgressAnimator.setDuration(1200);
        Log.d("MainActivity", "onCreateView");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Snapshot fragment", "onDestroyView");
    }
}
