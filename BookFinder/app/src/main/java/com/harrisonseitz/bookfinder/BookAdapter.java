package com.harrisonseitz.bookfinder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harrisonseitz on 6/27/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

  public BookAdapter(Activity context, List<Book> books) {
    super(context, 0, books);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItemView = convertView;
    if (listItemView == null) {
      listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_listing,
              parent, false);
    }

    final Book currBook = getItem(position);

    // Assemble child Image/TextViews
    // Title
    TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
    titleTextView.setText(currBook.getmTitle());
    // Author
    TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
    authorTextView.setText(currBook.getmAuthorName());
    // Description
    TextView descTextView = (TextView) listItemView.findViewById(R.id.book_desc);
    descTextView.setText(currBook.getmDescription());
    // Image
    String thumbnailUrl = currBook.getmThumbnailUrl();
    ImageView thumbnailImageView = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
    if (thumbnailUrl == "") {
      thumbnailImageView.setImageResource(R.drawable.ic_book_black_24dp);
    }
    else {
      Picasso.with(getContext()).load(thumbnailUrl).into(thumbnailImageView);
    }

    listItemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Uri detailsPage = Uri.parse(currBook.getmDetailsUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, detailsPage);
        if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
          v.getContext().startActivity(intent);
        }
      }
    });
    return listItemView;
  }
}
