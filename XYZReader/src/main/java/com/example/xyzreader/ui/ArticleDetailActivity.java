package com.example.xyzreader.ui;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

    private MyStoryAdapter MyStoryAdapter;
    private RecyclerView mList;
    LinearLayoutManager mLayoutManager;
    private Cursor mCursor;
    private int mStoryId;
    private String mAuthor;
    private String mBody;
    private String mTitle;
    private String mPhoto;
    private String mDate;
    private int mBookmark;
    private ArrayList<String> mBodyList  = new ArrayList<String>();
    private static final String SHARED_PREFERENCES_KEY = "key";
    private static final String SHARED_PREFERENCES_ID = "bookmark";
    // Some magic parameters used to parse the long body text string into paragraphs.
    private static final int PARAGRAPH_LENGTH = 500;
    private static final int PARAGRAPH_BASE = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // https://stackoverflow.com/questions/51318506/up-navigation-in-fragments-toolbar
        AppCompatActivity appCompatActivity = ((AppCompatActivity) this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Custom setting for the up navigation
        // Must do this to preserve scroll position in ArticleListActivity
        // https://stackoverflow.com/questions/30679133/override-up-button-in-action-bar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
            mDate = mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE).substring(0,4);

            // Get the position from the bookmark
            SharedPreferences settings = getApplicationContext().
                    getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
            mBookmark = settings.getInt(SHARED_PREFERENCES_ID + mTitle, 0);

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
            collapsingToolbar.setTitle(mTitle);

            // Load the image into the image view
            ImageView photoView = (ImageView) findViewById(R.id.photo);
            Picasso.get().load(mPhoto).into(photoView);

            // Parse the body text into a list, so that it can be displayed in a RecyclerView
            // This should be moved so that the list is parsed and THEN inserted into the database.
            String mText = mBody;
            // Add the author and published date as the first element
            mBodyList.add("\n" + mAuthor + " - " + mDate + "\n");
            String base = "";
            int cutoff = 0; // this is where the first break is found
            // This while loop is what is taking so long.
            while (mText.length() > PARAGRAPH_LENGTH) {
                base = mText.substring(0, PARAGRAPH_BASE);
                mText = mText.substring(PARAGRAPH_BASE);
                // The idea of parsing the body using \r\n\r\n and \r\n\ was from @siena on Slack
                cutoff = mText.indexOf("\r\n\r\n");
                base += mText.substring(0, cutoff);
                mText = mText.substring(cutoff);
                base = base.replaceAll("(\r\n)", " ");
                mBodyList.add(base);
            }
            mBodyList.add(mText);

            // Code for recycler view to display the body.
            mList = (RecyclerView) findViewById(R.id.rv_list);
            mLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            mList.setLayoutManager(mLayoutManager);
            mList.setHasFixedSize(true);
            MyStoryAdapter = new MyStoryAdapter(this, mBodyList);
            mList.setAdapter(MyStoryAdapter);
            mLayoutManager.scrollToPosition(mBookmark);
        }
    }

    public void bookmark(View view){
        int scroll_position = mLayoutManager.findFirstVisibleItemPosition();
        SharedPreferences settings = getApplicationContext().
                getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SHARED_PREFERENCES_ID + mTitle, scroll_position).commit();
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator),
                getResources().getString(R.string.bookmark_message) + " " +
                        + scroll_position
                        + " " + getResources().getString(R.string.of) + " " +
                        + mBodyList.size() + ".",
                Snackbar.LENGTH_LONG);
        snackbar.setAction(getResources().getString(R.string.close), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
