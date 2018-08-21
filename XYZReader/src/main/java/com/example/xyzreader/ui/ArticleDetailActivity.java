package com.example.xyzreader.ui;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.squareup.picasso.Picasso;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 * Need to implement: swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor mCursor;
    private int mStoryId;
    private String mAuthor;
    private String mBody;
    private String mTitle;
    private String mPhoto;
    private String mDate;
    public static final String KEY_TITLE = "title";
    public static final String KEY_STORY = "story";
    public static final String KEY_BODY = "body";
    public static final String KEY_DATE = "date";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_PHOTO = "photo";

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
            // The story has been loaded from the DB into a class variable, time to attach it to the fragment.
            attach_fragment();
        }
    }

    public void attach_fragment() {
        // Put the story in a new bundle
        Bundle arguments = new Bundle();
        arguments.putInt(KEY_STORY, mStoryId);
        arguments.putString(KEY_TITLE, mTitle);
        // Is this really slow?
        arguments.putString(KEY_BODY, mBody);
        arguments.putString(KEY_PHOTO, mPhoto);
        arguments.putString(KEY_AUTHOR, mAuthor);
        arguments.putString(KEY_DATE, mDate);
        // Create a new fragment
        ArticleDetailFragment articleDetailFragment = new ArticleDetailFragment();
        articleDetailFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, articleDetailFragment)
                .commit();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
    }
}
