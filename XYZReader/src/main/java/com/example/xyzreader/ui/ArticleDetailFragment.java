package com.example.xyzreader.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends android.support.v4.app.Fragment implements
        View.OnClickListener {

    public mAdapter mAdapter;
    public RecyclerView mList;
    LinearLayoutManager mLayoutManager;
    private View mRootView;
    private String mAuthor;
    private String mBody;
    private String mTitle;
    private String mPhoto;
    private String mDate;
    private int mScrollPosition;
    private String mText;

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
        mDate = getArguments().getString(ArticleDetailActivity.KEY_DATE).substring(0, 4);
        mAuthor = getArguments().getString(ArticleDetailActivity.KEY_AUTHOR);
        mPhoto = getArguments().getString(ArticleDetailActivity.KEY_PHOTO);
        mBody = getArguments().getString(ArticleDetailActivity.KEY_BODY);
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

        FloatingActionButton nextButton = (FloatingActionButton) mRootView.findViewById(R.id.floatingActionButton);
        // This view is for users that in some configuration other than portrait on a phone.
        nextButton.setOnClickListener(this);

        if (savedInstanceState != null) {

            int fromSIS = savedInstanceState.getInt("scroll");
            if (fromSIS>0) mScrollPosition = fromSIS;
        }
        Log.d("LOG", "asdf mScrollPosition on create view " + mScrollPosition);

        return mRootView;
    }

    private void bindViews() {
        if (mTitle != null) {

            //https://stackoverflow.com/questions/31504358/android-home-button-in-collapsing-toolbar-with-image
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(mAuthor + " - " + mDate);

            ImageView photoView = (ImageView) mRootView.findViewById(R.id.photo);
            Picasso.get().load(mPhoto).into(photoView);

            mText = mBody;

            ArrayList<String> list = new ArrayList<String>();
            int paragraph_length = 500;
            int base_length = 150;
            String base = "";
            int cutoff = 0; // this is where the first break is found
            while (mText.length() > paragraph_length) {
                base = mText.substring(0, base_length);
                mText = mText.substring(base_length);
                cutoff = mText.indexOf("\r\n\r\n");
                base += mText.substring(0, cutoff);
                mText = mText.substring(cutoff);
                base = base.replaceAll("(\r\n)", " ");
                list.add(base);
            }
            list.add(mText);

            //code for recycler view
            mList = (RecyclerView) mRootView.findViewById(R.id.rv_list);
            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);
            mList.setLayoutManager(mLayoutManager);
            mList.setHasFixedSize(true);
            mAdapter = new mAdapter(getActivity().getApplicationContext(), list);
            mList.setAdapter(mAdapter);
            mLayoutManager.scrollToPosition(123);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        int scrollPosition = mLayoutManager.findFirstVisibleItemPosition();
        if (scrollPosition>0) savedInstanceState.putInt("scroll", scrollPosition);
        mScrollPosition = scrollPosition;
        Log.d("LOG", "asdf onSaveinstancestate " + mScrollPosition);
    }


    /*
     *  The onClick method handles the button that adds the ingredients for this
     *  recipe to the widget, and the navigation buttons to the previous/next step.
     *
     *  @param mIngredients is already a class variable
     *  @returns nothing, but there is a Toast to the user
     * */
    @Override
    public void onClick(View v) {
        // get the first completely visible item
        int scrollPosition = mLayoutManager.findFirstVisibleItemPosition();
        // save that in the shared preferences
        /*Context context = getActivity().getApplicationContext();
        SharedPreferences settings = context.getSharedPreferences("key", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("bookmark", scrollPosition).commit();*/
        mLayoutManager.scrollToPosition(12);
        Log.d("LOG", "asdf bookmark: " + scrollPosition);
    }

}
