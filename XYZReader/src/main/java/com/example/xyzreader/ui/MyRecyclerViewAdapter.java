package com.example.xyzreader.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//TODO (3) extend RV View Holder (implement onCreate, onBind, and getItemCount)
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    //TODO (4b) Create ItemClickListener
    private ItemClickListener mClickListener;
    //TODO (2) get data from constructor
    private List<String> mTitles;
    private List<String> mDates;
    private List<String> mThumbs;
    private List<String> mAuthors;

    // Create MyRecyclerViewAdapter
    MyRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    //TODO (5) inflate
    // Inflates the cell layout from recyclerview_item.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_article, parent, false);
        return new ViewHolder(view);
    }

    // Binds each poster to the cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleView.setText(mTitles.get(position));
        // Set the author and date (using only the year)
        String author = mAuthors.get(position);
        String date = mDates.get(position);
        holder.subtitleView.setText(author + " - " + date);
        // Load the thumbnail using Picasso
        String thumbnail_url = mThumbs.get(position);
        Picasso.get().load(thumbnail_url).into(holder.thumbnailView);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mTitles != null) size = mThumbs.size();
        return size;
    }

    // TODO (6) create swap cursor method to reset the data
    void swapCursor(Cursor data) {
        // Move through the cursor and extract the movie poster urls.
        List<String> titles = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<String> thumbs = new ArrayList<>();
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            authors.add(data.getString(ArticleLoader.Query.AUTHOR));
            titles.add(data.getString(ArticleLoader.Query.TITLE));
            thumbs.add(data.getString(ArticleLoader.Query.THUMB_URL));
            dates.add(data.getString(ArticleLoader.Query.PUBLISHED_DATE).substring(0, 4));
        }
        mTitles = titles;
        mThumbs = thumbs;
        mDates = dates;
        mAuthors = authors;
        notifyDataSetChanged();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    //TODO (4a) Create interface for the click listener
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    //TODO (1) create ViewHolder class - implement OnClickListener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        // Set the click listener to the image view
        // When a user clicks on an image --> something happens based on the image a user clicked.
        ViewHolder(View view) {
            super(view);
            thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
            view.setOnClickListener(this);
        }

        //TODO (4c) 'wire' into ViewHolder
        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(getAdapterPosition());
        }
    }
}
