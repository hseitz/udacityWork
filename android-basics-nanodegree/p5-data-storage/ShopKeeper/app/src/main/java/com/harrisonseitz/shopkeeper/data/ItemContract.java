package com.harrisonseitz.shopkeeper.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by harrisonseitz on 8/8/17.
 */

public final class ItemContract {

  private ItemContract() {}

  public static final String CONTENT_AUTHORITY = "com.harrisonseitz.shopkeeper";

  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String PATH_ITEMS = "items";

  public static final class ItemEntry implements BaseColumns {
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

    // DB table name
    public final static String TABLE_NAME = "items";

    // Column names
    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_PRODUCT_NAME = "name";
    public final static String COLUMN_QUANTITY = "quantity";
    public final static String COLUMN_PRICE = "price";
    public final static String COLUMN_SUPPLIER_EMAIL = "supplierEmail";
    public final static String COLUMN_IMAGE = "image";

  }
}
