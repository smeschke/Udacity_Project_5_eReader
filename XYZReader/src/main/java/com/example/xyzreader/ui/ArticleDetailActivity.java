package com.example.xyzreader.ui;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 * Need to implement: swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String KEY_TITLE = "title";
    public static final String KEY_STORY = "story";
    public static final String KEY_BODY = "body";
    public static final String KEY_DATE = "date";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_PHOTO = "photo";
    public mAdapter mAdapter;
    public RecyclerView mList;
    LinearLayoutManager mLayoutManager;
    private Cursor mCursor;
    private int mStoryId;
    private String mAuthor;
    private String mBody;
    private String mTitle;
    private String mPhoto;
    private String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // Get the position of the story in the DB, this is passed from the ArticleListActivity
        mStoryId = getIntent().getExtras().getInt(ArticleListActivity.ID_KEY);
        // Initialize the loader manager
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Return a cursor that contains all the articles
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        // If the cursor is not null, get the story, author, etc... from the DB.
        if (cursor != null) {
            mCursor = cursor;
            mCursor.moveToPosition(mStoryId);
            mAuthor = mCursor.getString(ArticleLoader.Query.AUTHOR);
            mPhoto = mCursor.getString(ArticleLoader.Query.PHOTO_URL);
            mTitle = mCursor.getString(ArticleLoader.Query.TITLE);
            mBody = mCursor.getString(ArticleLoader.Query.BODY);
            mDate = mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE);
            bindViews();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
    }

    private void bindViews() {
        if (mTitle != null) {

            //https://stackoverflow.com/questions/31504358/android-home-button-in-collapsing-toolbar-with-image
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(mAuthor + " - " + mDate);

            // Load the image into the image view
            ImageView photoView = (ImageView) findViewById(R.id.photo);
            Picasso.get().load(mPhoto).into(photoView);

            // Parse the body text into a list, so that it can be displayed in a RecyclerView
            // This should be moved so that the list is parsed and THEN inserted into the database.
            String mText = mBody;
            ArrayList<String> list = new ArrayList<String>();
            int paragraph_length = 500;
            int base_length = 150;
            String base = "";
            int cutoff = 0; // this is where the first break is found
            while (mText.length() > paragraph_length) {
                base = mText.substring(0, base_length);
                mText = mText.substring(base_length);
                // The idea of parsing the body using \r\n\r\n and \r\n\ was from @siena on Slack
                cutoff = mText.indexOf("\r\n\r\n");
                base += mText.substring(0, cutoff);
                mText = mText.substring(cutoff);
                base = base.replaceAll("(\r\n)", " ");
                list.add(base);
            }
            list.add(mText);

            // Code for recycler view to display the body.
            mList = (RecyclerView) findViewById(R.id.rv_list);
            mLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            mList.setLayoutManager(mLayoutManager);
            mList.setHasFixedSize(true);
            mAdapter = new mAdapter(this, list);
            mList.setAdapter(mAdapter);
            mLayoutManager.scrollToPosition(123);
        }
    }

    public void bookmark(View view){
        int scroll_position = mLayoutManager.findFirstVisibleItemPosition();
        Log.d("LOG", "asdf scroll position: " + scroll_position);
    }
}
