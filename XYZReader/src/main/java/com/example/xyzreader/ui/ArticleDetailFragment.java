package com.example.xyzreader.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends android.support.v4.app.Fragment {

    private View mRootView;
    private String mAuthor;
    private String mBody;
    private String mTitle;
    private String mPhoto;
    private String mDate;

    private String mText;
    public mAdapter mAdapter;
    public RecyclerView mList;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString(ArticleDetailActivity.KEY_TITLE);
        mDate = getArguments().getString(ArticleDetailActivity.KEY_DATE);
        mAuthor = getArguments().getString(ArticleDetailActivity.KEY_AUTHOR);
        mPhoto = getArguments().getString(ArticleDetailActivity.KEY_PHOTO);
        mBody = getArguments().getString(ArticleDetailActivity.KEY_BODY);

        //mBody = mBody.substring(0, 2345);
        mDate = mDate.substring(0, 4);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        bindViews();
        return mRootView;
    }


    private void bindViews() {
        if (mTitle != null) {
            /*ImageView photoView = (ImageView) mRootView.findViewById(R.id.photo);
            TextView titleView = (TextView) mRootView.findViewById(R.id.article_title);
            TextView bylineView = (TextView) mRootView.findViewById(R.id.article_byline);
            titleView.setText(mTitle);
            bylineView.setText(mDate + " " + mAuthor);
            Picasso.get().load(mPhoto).into(photoView);*/


            //https://stackoverflow.com/questions/31504358/android-home-button-in-collapsing-toolbar-with-image
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(mAuthor + " - " + mDate);

            ImageView photoView = (ImageView) mRootView.findViewById(R.id.photo);
            Picasso.get().load(mPhoto).into(photoView);

            mText = mBody;
            //mText = mText.substring(0,3456);

            ArrayList<String> list = new ArrayList<String>();
            int paragraph_length = 500;
            int base_length = 150;
            String base = "";
            int cutoff = 0; // this is where the first break is found
            while (mText.length()>paragraph_length){
                base = mText.substring(0,base_length);
                mText = mText.substring(base_length);
                cutoff = mText.indexOf("\r\n\r\n");
                base += mText.substring(0,cutoff);
                mText = mText.substring(cutoff);
                base = base.replaceAll("(\r\n)", "");
                list.add(base);
                //list.add(mText.substring(0,paragraph_length));
                //mText = mText.substring(paragraph_length);
            }
            list.add(mText);

            //code for recycler view
            mList = (RecyclerView) mRootView.findViewById(R.id.rv_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);
            mList.setLayoutManager(layoutManager);
            mList.setHasFixedSize(true);
            mAdapter = new mAdapter(getActivity().getApplicationContext(), list);
            mList.setAdapter(mAdapter);
        }
    }
}
