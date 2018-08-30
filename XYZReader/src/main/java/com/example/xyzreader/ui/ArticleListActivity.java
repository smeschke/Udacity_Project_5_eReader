package com.example.xyzreader.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.UpdaterService;

/**
 * An activity representing a list of Articles.
 */
public class ArticleListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        MyRecyclerViewAdapter.ItemClickListener {

    public static final String ID_KEY = "id_key";
    private RecyclerView mRecyclerView;
    public MyRecyclerViewAdapter mAdapter;
    public Cursor mCursor;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        if (savedInstanceState == null) refresh();

        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(this);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        Log.d("LOG", "asdf onLoadFinished");
        mCursor = data;
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(int position) {
        Log.d("LOG", "asdf position: " + position);
        // User has clicked on a story, send them to that story with an intent
        Intent toStory = new Intent(ArticleListActivity.this, ArticleDetailActivity.class);
        // In the intent, package where the story is in the DB
        toStory.putExtra(ID_KEY, position);
        startActivity(toStory);
    }
}
