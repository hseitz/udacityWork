package com.harrisonseitz.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by harrisonseitz on 7/26/17.
 */

public final class HabitContract {
  public static final class DishesEntry implements BaseColumns {
    // dishes table name
    public static final String TABLE_NAME = "dishes";
    // dishes col names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ROOMMATE_NAME = "name";
    public static final String COLUMN_NO_OF_DISHES = "no_of_dishes";
    public static final String COLUMN_MISC_NOTES = "misc";
    // dishes misc notes constant values
    public static final int OUT_OF_SOAP = 0;
    public static final int REPLACE_SPONGES = 1;
    public static final int SINK_CLOGGED = 2;
  }

  public static final class GarbageEntry implements BaseColumns {
    // garbage table name
    public static final String TABLE_NAME = "garbage";
    // garbage col names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ROOMMATE_NAME = "name";
    public static final String COLUMN_LBS_OF_TRASH = "lbs_of_trash";
    public static final String COLUMN_MISC_NOTES = "misc";
    // garbage misc notes
    public static final int OUT_OF_BAGS = 0;
    public static final int CAN_IS_GROSS = 1;
    public static final int DUMPSTER_FULL = 2;
  }
}
