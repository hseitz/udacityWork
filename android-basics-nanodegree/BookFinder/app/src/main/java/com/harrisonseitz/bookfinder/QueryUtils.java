package com.harrisonseitz.bookfinder;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harrisonseitz on 6/27/17.
 */

public final class QueryUtils {
  private QueryUtils() {}

  private static final String LOG_TAG = QueryUtils.class.getSimpleName();

  public static List<Book> extractBooks(String urlString) {
    URL url = createUrl(urlString);

    List<Book> books = new ArrayList<>();

    try {
      try {
        JSONObject booksResponse = new JSONObject(executeHttpRequest(url));
        if (booksResponse.has("items")) {
          JSONArray booksItems = booksResponse.getJSONArray("items");
          for (int i = 0; i < booksItems.length(); i++) {
            JSONObject bookInfo = booksItems.getJSONObject(i).getJSONObject("volumeInfo");
            StringBuilder authors = new StringBuilder();
            if (bookInfo.has("authors")) {
              for (int j = 0; j < bookInfo.getJSONArray("authors").length(); j++) {
                if (authors.length() != 0) {
                  authors.append(", ");
                }
                authors.append(bookInfo.getJSONArray("authors").getString(j));
              }
            } else {
              authors.append("None");
            }
            String description = "None";
            if (bookInfo.has("description")) {
              if (bookInfo.getString("description").length() > 250) {
                description = bookInfo.getString("description").substring(0, 250);
              } else {
                description = bookInfo.getString("description");
              }
            }
            String imageLink = "";
            if (bookInfo.has("imageLinks")) {
              imageLink = bookInfo.getJSONObject("imageLinks").getString("thumbnail");
            }
            books.add(new Book(imageLink,
                    bookInfo.getString("title"),
                    authors.toString(),
                    description,
                    bookInfo.getString("infoLink")));
          }
        }
      } catch (IOException e) {
        Log.e(LOG_TAG, "Error closing input stream");
      }
    }
    catch(JSONException e){
        Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
      }
    return books;
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
        Log.e(LOG_TAG, "Error in trying to connect to Books API: "
                + urlConnection.getResponseCode());
      }
    }
    catch (IOException e) {
      Log.e(LOG_TAG, "Failed to make HTTP connection to retrieve book data");
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