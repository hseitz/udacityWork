package com.harrisonseitz.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.harrisonseitz.habittracker.data.HabitContract.DishesEntry;
import com.harrisonseitz.habittracker.data.HabitContract.GarbageEntry;

/**
 * Created by harrisonseitz on 7/26/17.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

  static final String DB_NAME = "habits.db";
  private static final int DB_VERSION = 0;

  public HabitDbHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String SQL_CREATE_DISHES_TABLE = "CREATE TABLE " + DishesEntry.TABLE_NAME + " (" +
            DishesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DishesEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
            DishesEntry.COLUMN_ROOMMATE_NAME + " TEXT NOT NULL, " +
            DishesEntry.COLUMN_NO_OF_DISHES + " INTEGER NOT NULL DEFAULT 0, " +
            DishesEntry.COLUMN_MISC_NOTES + " INTEGER);";

    final String SQL_CREATE_GARBAGE_TABLE = "CREATE TABLE " + GarbageEntry.TABLE_NAME + " (" +
            GarbageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            GarbageEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
            GarbageEntry.COLUMN_ROOMMATE_NAME + " TEXT NOT NULL, " +
            GarbageEntry.COLUMN_LBS_OF_TRASH + " INTEGER NOT NULL, " +
            GarbageEntry.COLUMN_MISC_NOTES + " INTEGER);";

    db.execSQL(SQL_CREATE_DISHES_TABLE);
    db.execSQL(SQL_CREATE_GARBAGE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    /* on update would happen for different months, or if starting over the tracking (e.g. new apt,
    new roommate, change in policy */
    db.execSQL("DROP TABLE IF EXISTS " + DishesEntry.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + GarbageEntry.TABLE_NAME);
    onCreate(db);
  }
}
