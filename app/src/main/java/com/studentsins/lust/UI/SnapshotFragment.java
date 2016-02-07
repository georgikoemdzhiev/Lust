package com.studentsins.lust.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studentsins.lust.R;

/**
 * Created by koemdzhiev on 07/02/16.
 */
public class SnapshotFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

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

        Log.d("MainActivity", "onCreate" + mPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snapshot_layout,container,false);
        TextView textView = (TextView) view.findViewById(R.id.snapshot_text);
        textView.setText("Fragment # " + mPage);
        Log.d("MainActivity", "onCreateView" + mPage);
        return view;
    }
}
