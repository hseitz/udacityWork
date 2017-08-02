package com.harrisonseitz.bookfinder;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

  public static final String LOG_TAG = MainActivity.class.getSimpleName();
  private static final String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";
  private BookAdapter bookAdapter;
  private ListView bookListView;
  private TextView emptyTextView;
  private ProgressBar loadingIndicator;
  private EditText searchBox;
  private Button searchButton;
  private String previousQuery;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // setup network connectivity checking variables
    final ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    // setup default state views
    bookListView = (ListView) findViewById(R.id.book_list);
    emptyTextView = (TextView) findViewById(R.id.no_books);
    loadingIndicator = (ProgressBar) findViewById(R.id.books_loading);
    searchBox = (EditText) findViewById(R.id.text_input);
    searchButton = (Button) findViewById(R.id.search_button);

    // create/set BookAdapter and EmptyView
    bookAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());
    bookListView.setAdapter(bookAdapter);
    bookListView.setEmptyView(emptyTextView);
    loadingIndicator.setVisibility(View.GONE);


    // Check initial connectivity status
    if (!isConnected) {
      bookListView.setVisibility(View.GONE);
      emptyTextView.setText(R.string.no_connection);
    }
    else {
      if (previousQuery == null) {
        previousQuery = "books";
      }
      Bundle inputArgs = new Bundle();
      inputArgs.putString("searchTerm", previousQuery);
      getLoaderManager().initLoader(0, inputArgs, this);
    }

    // Set search button OnClickListener
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isConnectedOnClick = cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
        if (!isConnectedOnClick) {
          bookListView.setVisibility(View.GONE);
          emptyTextView.setVisibility(View.VISIBLE);
          emptyTextView.setText(R.string.no_connection);
        }
        else {
          bookAdapter.clear();
          loadingIndicator.setVisibility(View.VISIBLE);
          emptyTextView.setVisibility(View.GONE);
          String encodedInput;
          String input = searchBox.getText().toString();
          try {
            encodedInput = URLEncoder.encode(input, "UTF-8");
            Bundle inputArgs = new Bundle();
            inputArgs.putString("searchTerm", encodedInput);
            getLoaderManager().restartLoader(0, inputArgs, MainActivity.this);
          } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "UTF-8 doesn't exist?");
          }
          InputMethodManager inputManager = (InputMethodManager)
                  getSystemService(Context.INPUT_METHOD_SERVICE);
          inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                  InputMethodManager.HIDE_NOT_ALWAYS);
        }
      }
    });
  }

  @Override
  public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
    previousQuery = GOOGLE_BOOKS_URL + args.getString("searchTerm") + "&maxResults=20";
    return new BookLoader(MainActivity.this, previousQuery);
  }

  @Override
  public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
    emptyTextView.setText(R.string.no_books);
    emptyTextView.setVisibility(View.VISIBLE);
    bookAdapter.clear();
    if (!data.isEmpty()) {
      bookAdapter.addAll(data);
      emptyTextView.setVisibility(View.GONE);
    }
    loadingIndicator.setVisibility(View.GONE);
  }

  @Override
  public void onLoaderReset(Loader<List<Book>> loader) {
    bookAdapter.clear();
  }
}
