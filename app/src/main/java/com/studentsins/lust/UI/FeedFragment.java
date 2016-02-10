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

import java.util.ArrayList;

/**
 * Created by koemdzhiev on 07/02/16.
 */
public class FeedFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
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

        ArrayList<String> users = new ArrayList<>();
        users.add("Georgi Koemdzhiev");
        users.add("Mariya Menova");
        users.add("Simeon Simeonov");
        users.add("Ivan Dqkov");
        users.add("Dymityr Vasilev");
        users.add("Petar Dimov");

        FeedCardAdapter adapter = new FeedCardAdapter(users,mActivity);

        mRefreshLayout.setAdapter(adapter);
        Log.d("MainActivity", "onCreateView" + mPage);
        return view;
    }
}
