package com.example.xyzreader.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;

import java.util.ArrayList;

public class mAdapter extends RecyclerView.Adapter<mAdapter.mAdapterViewHolder> {

    private final Context mContext;
    public ArrayList<String> output_list;

    //get stuff on list from Main Activity
    public mAdapter(@NonNull Context context, ArrayList<String> ol) {
        mContext = context;
        output_list = ol;
    }

    //when view holder is created, inflate the views
    @Override
    public mAdapter.mAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        layoutId = R.layout.list_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new mAdapterViewHolder(view);
    }

    //bind data to view holder
    @Override
    public void onBindViewHolder(mAdapter.mAdapterViewHolder holder, int position) {
        holder.textView.setText(output_list.get(position));
    }

    //How many? The size of the output_list.
    @Override
    public int getItemCount() {
        return output_list.size();
    }

    //setting up the recycler view, and clicks
    class mAdapterViewHolder extends RecyclerView.ViewHolder {
        //initialize views
        public final TextView textView;

        //super the views so that they can be bound - set click listener too
        mAdapterViewHolder(View view) {
            super(view);
            //image  and text views for tricks
            textView = (TextView) view.findViewById(R.id.list_item_text_view);
        }
    }
}