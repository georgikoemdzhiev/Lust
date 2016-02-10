package com.studentsins.lust.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        public CardViewHolder(View itemView) {
            super(itemView);
            mUserName = (TextView)itemView.findViewById(R.id.userName);
        }
    }
}
