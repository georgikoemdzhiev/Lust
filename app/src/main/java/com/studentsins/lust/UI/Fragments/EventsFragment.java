package com.studentsins.lust.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studentsins.lust.Adapters.EventsCardAdapter;
import com.studentsins.lust.R;
import com.studentsins.lust.Utils.ListenerCollection;

import java.util.ArrayList;

/**
 * This class will hold the Events fragment functionality
 * Created by koemdzhiev on 07/02/16.
 */
public class EventsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Context mActivity;
    private int mPage;
    public static final String ARG_PAGE = "ARG_PAGE";

    // Method that returns a new instance of the fragment - recommended way of creating a fragment
    public static EventsFragment newInstance(int page) {
        Bundle args = new Bundle();
        //args.putInt(ARG_PAGE, page);
        EventsFragment fragment = new EventsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mActivity = getActivity();
        Log.d("MainActivity", "onCreate");
    }
//OnCreateView - set up the recycler view and display content
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_layout,container,false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.eventsCardViewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(ListenerCollection.showHideCollapseFAB);

        ArrayList<String> users = new ArrayList<>();
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");
        users.add("42Bellow");


        EventsCardAdapter adapter = new EventsCardAdapter(users,mActivity);

        mRecyclerView.setAdapter(adapter);
        Log.d("MainActivity", "onCreateView");

        return view;
    }
}
