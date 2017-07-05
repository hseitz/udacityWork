package com.harrisonseitz.bookfinder;

import java.net.URL;

/**
 * Created by harrisonseitz on 6/27/17.
 */

public class Book {

  private String mThumbnailUrl;
  private String mTitle;
  private String mAuthorName;
  private String mDescription;
  private String mDetailsUrl;

  public Book(String thumbnailUrl, String title, String author, String description, String detailsUrl) {
    mThumbnailUrl = thumbnailUrl;
    mTitle = title;
    mAuthorName = author;
    mDescription = description;
    mDetailsUrl = detailsUrl;
  }
  public String getmThumbnailUrl() { return mThumbnailUrl; }
  public String getmTitle() { return mTitle; }
  public String getmAuthorName() { return mAuthorName; }
  public String getmDescription() { return mDescription; }
  public String getmDetailsUrl() { return mDetailsUrl; }
}
