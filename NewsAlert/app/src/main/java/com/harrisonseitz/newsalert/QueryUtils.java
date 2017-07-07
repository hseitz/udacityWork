package com.harrisonseitz.newsalert;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by harrisonseitz on 6/27/17.
 */

public final class QueryUtils {
  private QueryUtils() {}

  private static final String LOG_TAG = QueryUtils.class.getSimpleName();

  public static List<Story> extractStories(String urlString) {
    URL url = createUrl(urlString);

    List<Story> stories = new ArrayList<>();

    try {
      try {
        JSONObject storiesResponse = new JSONObject(executeHttpRequest(url));
        if (storiesResponse.getJSONObject("response").has("results")) {
          JSONArray storiesResults = storiesResponse.getJSONObject("response").getJSONArray("results");
          for (int i = 0; i < storiesResults.length(); i++) {
            JSONObject storyInfo = storiesResults.getJSONObject(i);
            // convert date from ISO8601 string to nicer format
            SimpleDateFormat isoParse = new SimpleDateFormat("yyyy-MM-dd");
            String readableDate = "";
            try {
              Date dateIn = isoParse.parse(storyInfo.getString("webPublicationDate").substring(0,9));
              SimpleDateFormat readableFormat = new SimpleDateFormat("MMM dd, yyyy");
              readableDate = readableFormat.format(dateIn);
            }
            catch (ParseException e) {
              Log.e(LOG_TAG, "Failed to parse date" + e);
            }
            // add new Story to stories
            stories.add(new Story(storyInfo.getString("webTitle"),
                    "Published on: " + readableDate,
                    "Filed under section: " + storyInfo.getString("sectionName"),
                    storyInfo.getString("webUrl")
            ));
          }
        }
      } catch (IOException e) {
        Log.e(LOG_TAG, "Error closing input stream");
      }
    }
    catch(JSONException e){
      Log.e(LOG_TAG, "Problem parsing the story JSON results", e);
    }
    return stories;
  }


  private static String executeHttpRequest(URL url) throws IOException {
    String jsonResponse = "";
    if (url == null) {
      return jsonResponse;
    }
    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();
      if (urlConnection.getResponseCode() == 200) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      }
      else {
        Log.e(LOG_TAG, "Error in trying to connect to Guardian API: "
                + urlConnection.getResponseCode());
      }
    }
    catch (IOException e) {
      Log.e(LOG_TAG, "Failed to make HTTP connection to retrieve news data");
    }
    finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return jsonResponse;
  }

  private static String readFromStream(InputStream inputStream) throws IOException {
    StringBuilder output = new StringBuilder();
    if (inputStream != null) {
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
      BufferedReader reader = new BufferedReader(inputStreamReader);
      String line = reader.readLine();
      while (line != null) {
        output.append(line);
        line = reader.readLine();
      }
    }
    return output.toString();
  }

  private static URL createUrl(String stringUrl) {
    URL url = null;
    try {
      url = new URL(stringUrl);
    }
    catch(MalformedURLException e) {
      Log.e(LOG_TAG, "Error creating URL");
    }
    return url;
  }
}