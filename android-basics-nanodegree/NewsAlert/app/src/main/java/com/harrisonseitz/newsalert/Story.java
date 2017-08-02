package com.harrisonseitz.newsalert;

/**
 * Created by harrisonseitz on 7/7/17.
 */

public class Story {

  private String mTitle;
  private String mPubDate;
  private String mSection;
  private String mWebUrl;

  public Story(String title, String pubDate, String section, String webUrl) {
    mTitle = title;
    mPubDate = pubDate;
    mSection = section;
    mWebUrl = webUrl;
  }

  public String getmTitle() { return mTitle; }
  public String getmPubDate() { return mPubDate; }
  public String getmSection() { return mSection; }
  public String getmWebUrl() { return mWebUrl; }
}
