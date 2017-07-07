package com.harrisonseitz.newsalert;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by harrisonseitz on 7/7/17.
 */

public class StoryLoader extends AsyncTaskLoader<List<Story>> {
  private String loadUrl = "";

  public StoryLoader(Context context, String url){
    super(context);
    loadUrl = url;
  }

  @Override
  public List<Story> loadInBackground() {
    if (loadUrl == null) {
      return null;
    }
    List<Story> list = QueryUtils.extractStories(loadUrl);
    return list;
  }

  @Override
  protected void onStartLoading() {
    forceLoad();
  }
}
