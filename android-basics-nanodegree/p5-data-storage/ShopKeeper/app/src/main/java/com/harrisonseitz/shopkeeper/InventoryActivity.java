package com.harrisonseitz.shopkeeper;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.harrisonseitz.shopkeeper.data.ItemContract.ItemEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

  // BK view bindings
  @BindView(R.id.list) ListView inventoryList;
  @BindView(R.id.empty_view) View emptyView;
  private String[] projection = {
          ItemEntry._ID,
          ItemEntry.COLUMN_PRODUCT_NAME,
          ItemEntry.COLUMN_QUANTITY,
          ItemEntry.COLUMN_PRICE,
          ItemEntry.COLUMN_SUPPLIER_EMAIL,
          ItemEntry.COLUMN_IMAGE
  };

  private InventoryCursorAdapter inventoryCursorAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory);
    ButterKnife.bind(this);

    // setup fab
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);
        startActivity(intent);
      }
    });

    // setup ListView
    inventoryList.setEmptyView(emptyView);
    inventoryCursorAdapter = new InventoryCursorAdapter(this, null);
    inventoryList.setAdapter(inventoryCursorAdapter);

    getLoaderManager().initLoader(0, null, this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_inventory, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.inventory_menu_new) {
      Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  // Loader callbacks


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, ItemEntry.CONTENT_URI, projection, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    inventoryCursorAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    inventoryCursorAdapter.swapCursor(null);
  }
}
