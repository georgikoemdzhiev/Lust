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
import com.studentsins.lust.R;
import com.studentsins.lust.Utils.ListenerCollection;

import java.util.ArrayList;

/**
 * Created by koemdzhiev on 07/02/16.
 */
public class FeedFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String TAG = FeedFragment.class.getSimpleName();
    private int mPage;
    private RecyclerView mRecyclerView;
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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.feedCardViewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(ListenerCollection.showHideFAB);

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

        mRecyclerView.setAdapter(adapter);
        Log.d("MainActivity", "onCreateView" + mPage);
        return view;
    }
}
