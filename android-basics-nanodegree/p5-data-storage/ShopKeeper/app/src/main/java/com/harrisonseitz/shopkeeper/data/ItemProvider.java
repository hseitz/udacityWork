package com.harrisonseitz.shopkeeper.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.harrisonseitz.shopkeeper.data.ItemContract.ItemEntry;

/**
 * Created by harrisonseitz on 8/8/17.
 */

public class ItemProvider extends ContentProvider {

  public static final String LOG_TAG = ItemProvider.class.getSimpleName();

  private static final int ALL_ITEMS = 0;

  private static final int SINGLE_ITEM = 1;

  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    // URI for all items
    uriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS, ALL_ITEMS);
    // URI for single items
    uriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS + "/#", SINGLE_ITEM);
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    final int match = uriMatcher.match(uri);
    switch (match) {
      case ALL_ITEMS:
        return ItemEntry.CONTENT_LIST_TYPE;
      case SINGLE_ITEM:
        return ItemEntry.CONTENT_ITEM_TYPE;
      default:
        throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
    }
  }

  private ItemDbHelper dbHelper;

  @Override
  public boolean onCreate() {
    dbHelper = new ItemDbHelper(getContext());
    return true;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

    SQLiteDatabase db = dbHelper.getReadableDatabase();

    Cursor cursor;

    int match = uriMatcher.match(uri);
    switch (match) {
      case ALL_ITEMS:
        cursor = db.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null,
                sortOrder);
        break;
      case SINGLE_ITEM:
        selection = ItemEntry._ID + "=?";
        selectionArgs = new String[] {
                String.valueOf(ContentUris.parseId(uri))
        };
        cursor = db.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null,
                sortOrder);
        break;
      default:
        throw new IllegalArgumentException("Cannot query unknown URI " + uri);
    }

    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    final int match = uriMatcher.match(uri);
    switch (match) {
      case ALL_ITEMS:
        return insertItem(uri, values);
      default:
        throw new IllegalArgumentException("Insertion is not supported for " + uri);
    }
  }

  private Uri insertItem(Uri uri, ContentValues values) {
    String name = values.getAsString(ItemEntry.COLUMN_PRODUCT_NAME);
    if (name == null) {
      throw new IllegalArgumentException("Product requires a name");
    }

    Integer quantity = values.getAsInteger(ItemEntry.COLUMN_QUANTITY);
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }

    Integer price = values.getAsInteger(ItemEntry.COLUMN_PRICE);
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();

    long id = db.insert(ItemEntry.TABLE_NAME, null, values);
    if (id == -1) {
      Log.e(LOG_TAG, "Failed to insert row for " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return ContentUris.withAppendedId(uri, id);
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
    final int match = uriMatcher.match(uri);
    switch (match) {
      case ALL_ITEMS:
        getContext().getContentResolver().notifyChange(uri, null);
        return updateItem(uri, values, selection, selectionArgs);
      case SINGLE_ITEM:
        selection = ItemEntry._ID + "=?";
        selectionArgs = new String[] {
                String.valueOf(ContentUris.parseId(uri))
        };
        return updateItem(uri, values, selection, selectionArgs);
      default:
        throw new IllegalArgumentException("Update is not supported for " + uri);
    }
  }

  private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    if (values.containsKey(ItemEntry.COLUMN_PRODUCT_NAME)) {
      String name = values.getAsString(ItemEntry.COLUMN_PRODUCT_NAME);
      if (name == null) {
        throw new IllegalArgumentException("Product requires a name");
      }
    }

    if (values.containsKey(ItemEntry.COLUMN_QUANTITY)) {
      Integer quantity = values.getAsInteger(ItemEntry.COLUMN_QUANTITY);
      if (quantity == null) {
        throw new IllegalArgumentException("Quantity cannot be null");
      }
      else if (quantity < 0) {
        throw new IllegalArgumentException("Quantity cannot be negative");
      }
    }

    if (values.containsKey(ItemEntry.COLUMN_PRICE)) {
      Integer price = values.getAsInteger(ItemEntry.COLUMN_PRICE);
      if (price == null) {
        throw new IllegalArgumentException("Price cannot be null");
      }
      else if (price < 0) {
        throw new IllegalArgumentException("Price cannot be negative");
      }
    }

    if (values.size() == 0) {
      return 0;
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int rowsUpdated = db.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
    if (rowsUpdated > 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsUpdated;

  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    final int match = uriMatcher.match(uri);
    int rowsDeleted;
    switch (match) {
      case ALL_ITEMS:
        rowsDeleted = db.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted > 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
      case SINGLE_ITEM:
        selection = ItemEntry._ID + "=?";
        selectionArgs = new String[] {
          String.valueOf(ContentUris.parseId(uri))
        };
        rowsDeleted = db.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted > 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
      default:
        throw new IllegalArgumentException("Deletion not supported for " + uri);
    }
  }
}
