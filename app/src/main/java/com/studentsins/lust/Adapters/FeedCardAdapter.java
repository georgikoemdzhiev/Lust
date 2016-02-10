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
 * Created by koemdzhiev on 10/02/16.
 */
public class FeedCardAdapter extends RecyclerView.Adapter<FeedCardAdapter.CardViewHolder> {
    private ArrayList<String> users;
    public Context mContext;

    public FeedCardAdapter(ArrayList<String> users, Context context) {
        this.users = users;
        mContext = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_layout,parent,false);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        String name = users.get(position);
        holder.mUserName.setText(name);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder {
        protected TextView mUserName;
        protected Toolbar mToolbar;

        public CardViewHolder(View itemView) {
            super(itemView);
            mUserName = (TextView)itemView.findViewById(R.id.userName);
            mToolbar = (Toolbar)itemView.findViewById(R.id.user_toolbar);
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
