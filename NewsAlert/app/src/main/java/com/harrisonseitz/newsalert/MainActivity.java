package com.harrisonseitz.newsalert;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {
  @BindView(R.id.search_box) SearchView searchBox;
  @BindView(R.id.story_list) ListView storyList;
  @BindView(R.id.no_stories) TextView noStories;
  @BindView(R.id.stories_loading) ProgressBar loadingIndicator;
  @BindString(R.string.no_connection) String no_connection;
  @BindString(R.string.no_returned_stories) String no_stories;
  @BindString(R.string.search_hint) String search_hint;

  private String previousQuery;
  private StoryAdapter storyAdapter;
  private static final String GUARDIAN_NEWS_API_URL = "http://content.guardianapis.com/search?q=";
  private static final String GUARDIAN_NEWS_API_KEY = "test";
  public static final String LOG_TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    final ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    // setup adapter/emptyview, searchbox hint, and loading initial state
    storyAdapter = new StoryAdapter(MainActivity.this, new ArrayList<Story>());
    storyList.setAdapter(storyAdapter);
    storyList.setEmptyView(noStories);
    loadingIndicator.setVisibility(GONE);
    searchBox.setQueryHint(search_hint);

    // check initial connectivity & perform default query

    if (!isConnected) {
      storyList.setVisibility(GONE);
      noStories.setText(no_connection);
    }
    else {
      if (previousQuery == null) {
        previousQuery = "Android";
      }
      Bundle inputArgs = new Bundle();
      inputArgs.putString("searchTerm", previousQuery);
      getLoaderManager().initLoader(0, inputArgs, this);
    }

    // setup search listener
    searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        boolean isConnectedOnSearch = cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
        if (!isConnectedOnSearch) {
          storyList.setVisibility(GONE);
          noStories.setVisibility(VISIBLE);
          noStories.setText(no_connection);
        }
        else {
          storyAdapter.clear();
          loadingIndicator.setVisibility(VISIBLE);
          noStories.setVisibility(GONE);
          String encodedInput;
          String input = searchBox.getQuery().toString();
          try {
            encodedInput = URLEncoder.encode(input, "UTF-8");
            Bundle inputArgs = new Bundle();
            inputArgs.putString("searchTerm", encodedInput);
            getLoaderManager().restartLoader(0, inputArgs, MainActivity.this);
          }
          catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "UTF-8?");
          }
          InputMethodManager inputManager = (InputMethodManager)
                  getSystemService(Context.INPUT_METHOD_SERVICE);
          inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                  InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
  }

  @Override
  public Loader<List<Story>> onCreateLoader(int id, Bundle args) {
    previousQuery = GUARDIAN_NEWS_API_URL + args.getString("searchTerm") +
            "&api-key=" + GUARDIAN_NEWS_API_KEY;
    return new StoryLoader(MainActivity.this, previousQuery);
  }

  @Override
  public void onLoadFinished(Loader<List<Story>> loader, List<Story> data) {
    noStories.setText(no_stories);
    noStories.setVisibility(VISIBLE);
    storyAdapter.clear();
    if (!data.isEmpty()) {
      storyAdapter.addAll(data);
      noStories.setVisibility(GONE);
    }
    loadingIndicator.setVisibility(GONE);
  }

  @Override
  public void onLoaderReset(Loader<List<Story>> loader) {
    storyAdapter.clear();
  }
}
