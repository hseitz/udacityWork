package com.harrisonseitz.shopkeeper;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harrisonseitz.shopkeeper.data.ItemContract.ItemEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by harrisonseitz on 8/9/17.
 */

public class InventoryCursorAdapter extends CursorAdapter {

  public static final String LOG_TAG = InventoryCursorAdapter.class.getSimpleName();
  private Context adapterContext;

  public InventoryCursorAdapter(Context context, Cursor cursor) {
    super(context, cursor, 0);
    this.adapterContext = context;
  };

  class ViewHolder {
    @BindView(R.id.list_product_name) TextView productNameTextView;
    @BindView(R.id.list_quantity) TextView quantityTextView;
    @BindView(R.id.list_price) TextView priceTextView;
    @BindView(R.id.sell_item_button) ImageView sellItemButton;
    @BindView(R.id.inventory_details) LinearLayout inventoryDetails;

    public ViewHolder(@NonNull View v) {
      ButterKnife.bind(this, v);
    }
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    View v = LayoutInflater.from(context).inflate(R.layout.inventory_list_item, parent, false);
    ViewHolder viewHolder = new ViewHolder(v);
    v.setTag(viewHolder);
    return v;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    ViewHolder viewHolder = (ViewHolder) view.getTag();

    // get data from cursor
    final int productId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
    final String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
    final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
    int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
    String imageUriString = cursor.getString(cursor.getColumnIndexOrThrow("image"));
    String formattedPrice = "$" + String.valueOf(price);

    // set data on views
    viewHolder.productNameTextView.setText(productName);
    viewHolder.quantityTextView.setText(String.valueOf(quantity));
    viewHolder.priceTextView.setText(formattedPrice);

    viewHolder.sellItemButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Uri sellItemClickedUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, productId);
        if (quantity > 0) {
          int newQuantity = quantity - 1;
          ContentValues values = new ContentValues();
          values.put(ItemEntry.COLUMN_QUANTITY, newQuantity);
          int rowsUpdated = adapterContext.getContentResolver().update(sellItemClickedUri, values,
                  null, null);
          if (rowsUpdated < 0){
            Log.e(LOG_TAG, "Error updating quantity for " + productId);
          }
        }
      }
    });

    viewHolder.inventoryDetails.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Uri itemClickedUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, productId);
        Intent intent = new Intent(adapterContext, EditorActivity.class);
        intent.setData(itemClickedUri);
        adapterContext.startActivity(intent);
      }
    });
  }
}
