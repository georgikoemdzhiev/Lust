package com.studentsins.lust.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studentsins.lust.Adapters.FeedCardAdapter;
import com.studentsins.lust.MainActivity;
import com.studentsins.lust.R;

import java.util.ArrayList;

/**
 * Created by koemdzhiev on 07/02/16.
 */
public class FeedFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String TAG = FeedFragment.class.getSimpleName();
    private int mPage;
    private RecyclerView mRefreshLayout;
    private Context mActivity;

    public static FeedFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mActivity = getActivity();
        Log.d("MainActivity", "onCreate" + mPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_layout,container,false);

        mRefreshLayout = (RecyclerView) view.findViewById(R.id.feedCardViewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRefreshLayout.setLayoutManager(linearLayoutManager);

        mRefreshLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy >= 0) {
                    // Scrolling up.. hide the FAB
                    MainActivity.mFloatingActionsMenu.animate()
                            .setDuration(150)
                            .translationY(300);
                } else {
                    // Scrolling down.. show the FAB
                    MainActivity.mFloatingActionsMenu.animate()
                            .setDuration(150)
                            .translationY(0);
                }

                Log.d(TAG,"DY value: "+dy);

            }
        });
        ArrayList<String> users = new ArrayList<>();
        users.add("Georgi Koemdzhiev");
        users.add("Mariya Menova");
        users.add("Simeon Simeonov");
        users.add("Ivan Dqkov");
        users.add("Dymityr Vasilev");
        users.add("Petar Dimov");
        users.add("Stoyan Stoyanov");
        users.add("Alexander Lunar");
        users.add("Awesome Jhon");

        FeedCardAdapter adapter = new FeedCardAdapter(users,mActivity);

        mRefreshLayout.setAdapter(adapter);
        Log.d("MainActivity", "onCreateView" + mPage);
        return view;
    }
}
