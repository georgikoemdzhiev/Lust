package com.studentsins.lust.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.studentsins.lust.R;

import java.util.ArrayList;

/**
 * Adapter class to handle the feed's recycler view
 * Created by koemdzhiev on 10/02/16.
 */
public class FeedCardAdapter extends RecyclerView.Adapter<FeedCardAdapter.CardViewHolder> {
    public Context mContext;
    private ArrayList<String> users;

    //Constructor...
    public FeedCardAdapter(ArrayList<String> users, Context context) {
        this.users = users;
        mContext = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feed_card_layout, parent, false);

        return new CardViewHolder(view);
    }

    //Set up the list item data...
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        String name = users.get(position);
        holder.mUserName.setText(name);
    }

    //Return the size of the list...
    @Override
    public int getItemCount() {
        return users.size();
    }

    //ViewHolder class to hold the list item data...
    public class CardViewHolder extends RecyclerView.ViewHolder {
        protected TextView mUserName;
        protected Toolbar mToolbar;

        public CardViewHolder(View itemView) {
            super(itemView);
            mUserName = (TextView) itemView.findViewById(R.id.vendor_name);
            mToolbar = (Toolbar) itemView.findViewById(R.id.event_toolbar);
            mToolbar.inflateMenu(R.menu.menu_feed_user);

            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    //noinspection SimplifiableIfStatement
                    if (id == R.id.action_settings) {
                        Toast.makeText(mContext, "Card menu click!", Toast.LENGTH_LONG).show();
                        return true;
                    }

                    return false;
                }
            });
        }
    }
}
