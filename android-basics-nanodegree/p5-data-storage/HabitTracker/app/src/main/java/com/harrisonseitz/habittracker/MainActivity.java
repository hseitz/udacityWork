package com.harrisonseitz.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.harrisonseitz.habittracker.data.HabitContract.DishesEntry;
import com.harrisonseitz.habittracker.data.HabitContract.GarbageEntry;
import com.harrisonseitz.habittracker.data.HabitDbHelper;

public class MainActivity extends AppCompatActivity {

  private final String LOG_TAG = MainActivity.class.getSimpleName();

  private HabitDbHelper mDbHelper;

  // dummy values shared for dishes/garbage entries
  private int date = 1501175361;
  private String roommateName = "Harrison";
  // dummy values for dishes entries
  private int dishesNumber = 15;
  private int dishesMiscNotes = 0;
  // dummy values for garbage entries
  private int garbagePounds = 20;
  private int garbageMiscNotes = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public Cursor readHabitEntries(int habitType){
    mDbHelper = new HabitDbHelper(this);
    SQLiteDatabase db = mDbHelper.getReadableDatabase();

    String[] dishesColumns = {
            DishesEntry._ID,
            DishesEntry.COLUMN_DATE,
            DishesEntry.COLUMN_ROOMMATE_NAME,
            DishesEntry.COLUMN_NO_OF_DISHES,
            DishesEntry.COLUMN_MISC_NOTES };

    String[] garbageColumns = {
            GarbageEntry._ID,
            GarbageEntry.COLUMN_DATE,
            GarbageEntry.COLUMN_ROOMMATE_NAME,
            GarbageEntry.COLUMN_LBS_OF_TRASH,
            GarbageEntry.COLUMN_MISC_NOTES };
    // 0 = dishes, 1 = garbage
    Cursor cursor;
    if (habitType == 0) {
      // return the entire dishes entries table
      cursor = db.query(
              DishesEntry.TABLE_NAME,
              dishesColumns,
              null,
              null,
              null,
              null,
              null);
    }
    else {
      // return the entire garbage entries table
      cursor = db.query(
              GarbageEntry.TABLE_NAME,
              garbageColumns,
              null,
              null,
              null,
              null,
              null);
    }
    return cursor;
  }

  private void writeHabitEntry(int habitType) {
    mDbHelper = new HabitDbHelper(this);
    SQLiteDatabase db = mDbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    // 0 = dishes, 1 = garbage
    if (habitType == 0) {
      values.put(DishesEntry.COLUMN_DATE, date);
      values.put(DishesEntry.COLUMN_ROOMMATE_NAME, roommateName);
      values.put(DishesEntry.COLUMN_NO_OF_DISHES, dishesNumber);
      values.put(DishesEntry.COLUMN_MISC_NOTES, dishesMiscNotes);
      long newRowId = db.insert(DishesEntry.TABLE_NAME, null, values);
      if (newRowId == -1) {
        Log.e(LOG_TAG, "Unable to save dishes habit");
      }
    }
    else {
      values.put(GarbageEntry.COLUMN_DATE, date);
      values.put(GarbageEntry.COLUMN_ROOMMATE_NAME, roommateName);
      values.put(GarbageEntry.COLUMN_LBS_OF_TRASH, garbagePounds);
      values.put(GarbageEntry.COLUMN_MISC_NOTES, garbageMiscNotes);
      long newRowId = db.insert(GarbageEntry.TABLE_NAME, null, values);
      if (newRowId == -1) {
        Log.e(LOG_TAG, "Unable to save garbage habit");
      }
    }
  }
}