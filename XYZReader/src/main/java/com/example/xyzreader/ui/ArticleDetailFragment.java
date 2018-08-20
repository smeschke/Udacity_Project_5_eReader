package com.example.xyzreader.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        mBody = mBody.replaceAll("(\r\n|\n)", "");
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
            ImageView photoView = (ImageView) mRootView.findViewById(R.id.photo);
            TextView titleView = (TextView) mRootView.findViewById(R.id.article_title);
            TextView bylineView = (TextView) mRootView.findViewById(R.id.article_byline);
            //TextView bodyView = (TextView) mRootView.findViewById(R.id.article_body);
            titleView.setText(mTitle);
            bylineView.setText(mDate + " " + mAuthor);
            //bodyView.setText(mBody);
            Picasso.get().load(mPhoto).into(photoView);

            mText = mBody;

            ArrayList<String> list = new ArrayList<String>();
            int paragraph_length = 345;
            while (mText.length()>paragraph_length){
                list.add(mText.substring(0,paragraph_length) + "\n\n");
                mText = mText.substring(paragraph_length);
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
