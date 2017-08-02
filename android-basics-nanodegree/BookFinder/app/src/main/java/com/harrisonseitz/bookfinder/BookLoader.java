package com.harrisonseitz.bookfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by harrisonseitz on 6/27/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
  private String loadUrl = "";

  public BookLoader(Context context, String url) {
    super(context);
    loadUrl = url;
  }

  @Override
  public List<Book> loadInBackground() {
    if (loadUrl == null) {
      return null;
    }
    List<Book> list = QueryUtils.extractBooks(loadUrl);
    return list;
  }

  @Override
  protected void onStartLoading() {
    forceLoad();
  }
}
