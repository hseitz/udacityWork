package com.harrisonseitz.newsalert;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by harrisonseitz on 7/7/17.
 */

public class StoryAdapter extends ArrayAdapter<Story> {

  public StoryAdapter(Activity context, List<Story> stories) { super(context, 0, stories); }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView != null) {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    else {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.story, parent, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    }

    final Story currStory = getItem(position);

    viewHolder.storyTitle.setText(currStory.getmTitle());
    viewHolder.storyPubDate.setText(currStory.getmPubDate());
    viewHolder.storySection.setText(currStory.getmSection());

    convertView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Uri storyPage = Uri.parse(currStory.getmWebUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, storyPage);
        if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
          v.getContext().startActivity(intent);
        }
      }
    });

    return convertView;
  }

  class ViewHolder {
    @BindView(R.id.story_title_text) TextView storyTitle;
    @BindView(R.id.story_pub_date) TextView storyPubDate;
    @BindView(R.id.story_section) TextView storySection;

    public ViewHolder(@NonNull View v) {
      ButterKnife.bind(this, v);
    }
  }
}

