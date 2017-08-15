package com.harrisonseitz.shopkeeper.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.harrisonseitz.shopkeeper.data.ItemContract.ItemEntry;

/**
 * Created by harrisonseitz on 8/8/17.
 */

public class ItemDbHelper extends SQLiteOpenHelper {

  public static final String DATABASE_NAME = "inventory.db";
  public static final int DATABASE_VERSION = 1;

  public ItemDbHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}

  @Override
  public void onCreate(SQLiteDatabase db) {
    String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " ("
            + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ItemEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
            + ItemEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
            + ItemEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
            + ItemEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL, "
            + ItemEntry.COLUMN_IMAGE + " TEXT NOT NULL);";

    db.execSQL(SQL_CREATE_ITEMS_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Just drop and recreate
    db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
    onCreate(db);
  }
}
