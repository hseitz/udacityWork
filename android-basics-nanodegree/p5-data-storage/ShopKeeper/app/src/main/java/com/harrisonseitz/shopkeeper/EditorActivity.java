package com.harrisonseitz.shopkeeper;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.harrisonseitz.shopkeeper.data.ItemContract.ItemEntry;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * Created by harrisonseitz on 8/8/17.
 */


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  // BK bindings
  // Editor view bindings
  @BindView(R.id.edit_product_name) EditText productNameEditText;
  @BindView(R.id.edit_quantity) EditText quantityEditText;
  @BindView(R.id.edit_list_price) EditText listPriceEditText;
  @BindView(R.id.edit_supplier_email) EditText supplierEmailEditText;
  @BindView(R.id.open_po_button) TextView openPoButton;
  @BindView(R.id.add_image_button) TextView addImageButton;
  @BindView(R.id.image_container) ImageView imageContainer;
  @BindView(R.id.editor_increment_button) TextView incrementButton;
  @BindView(R.id.editor_decrement_button) TextView decrementButton;


  // String bindings
  @BindString(R.string.editor_add_item) String add_item;
  @BindString(R.string.editor_edit_item) String edit_item;
  @BindString(R.string.unsaved_changes) String unsaved_changes;
  @BindString(R.string.unsaved_changes_discard) String unsaved_changes_discard;
  @BindString(R.string.keep_editing) String keep_editing;
  @BindString(R.string.editor_error_save_new) String error_save_new;
  @BindString(R.string.editor_success_save_new) String success_save_new;
  @BindString(R.string.editor_error_update) String error_update;
  @BindString(R.string.editor_success_update) String success_update;
  @BindString(R.string.editor_error_delete) String error_delete;
  @BindString(R.string.editor_success_update) String success_delete;
  @BindString(R.string.editor_delete_button) String delete_button;
  @BindString(R.string.editor_cancel_button) String cancel_button;

  // Member Variables
  private String LOG_TAG = EditorActivity.class.getSimpleName();
  private Uri currentItemUri;
  private String defaultQuantity = "0";
  private String defaultPrice = "0";
  private boolean currentItemHasChanged = false;
  private String[] projection = {
          ItemEntry._ID,
          ItemEntry.COLUMN_PRODUCT_NAME,
          ItemEntry.COLUMN_QUANTITY,
          ItemEntry.COLUMN_PRICE,
          ItemEntry.COLUMN_SUPPLIER_EMAIL,
          ItemEntry.COLUMN_IMAGE
  };
  private int imageRequestCode = 1;
  private Uri currentItemImageUri;

  // OnTouchListener for dirty edit states
  @OnTouch({R.id.edit_product_name, R.id.edit_quantity, R.id.edit_list_price,
          R.id.edit_supplier_email, R.id.image_container })
  public boolean onTouch() {
    currentItemHasChanged = true;
    return false;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory_editor);
    ButterKnife.bind(this);

    // Process intent if opened via tapping list item
    Intent intent = getIntent();
    Uri incomingItemUri = intent.getData();

    openPoButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        String recipientEmail = supplierEmailEditText.getText().toString();
        intent.setData(Uri.parse("mailto:" + recipientEmail));
        intent.putExtra(Intent.EXTRA_EMAIL, recipientEmail);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Order Request for " + productNameEditText.getText());
        intent.putExtra(Intent.EXTRA_TEXT,"To whom it may concern,\n\nI need to place an order for __ additional units of "+ productNameEditText.getText() +". Please send us an invoice at your earliest convenience.");
        if (intent.resolveActivity(getPackageManager()) != null) {
          v.getContext().startActivity(intent);
        }
      }
    });

    decrementButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        decrementQuantity();
      }
    });

    incrementButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        incrementQuantity();
      }
    });

    addImageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent imageIntent;
        // Use ACTION_OPEN_DOCUMENT + set category CATEGORY_OPENABLE for device >= API 19
        if (Build.VERSION.SDK_INT >= 19) {
          imageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
          imageIntent.addCategory(Intent.CATEGORY_OPENABLE);
          imageIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }
        // else use ACTION_GET_CONTENT
        else {
          imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        imageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // set mime type to image/*
        imageIntent.setType("image/*");
        // go get the image
        startActivityForResult(imageIntent, imageRequestCode);
      }
    });

    if (incomingItemUri == null) {
      setTitle(add_item);
      invalidateOptionsMenu();
      openPoButton.setVisibility(View.GONE);
      incrementButton.setVisibility(View.GONE);
      decrementButton.setVisibility(View.GONE);
    }
    else {
      setTitle(edit_item);
      currentItemUri = incomingItemUri;
      getLoaderManager().initLoader(0, null, this);
      openPoButton.setVisibility(View.VISIBLE);
      incrementButton.setVisibility(View.VISIBLE);
      decrementButton.setVisibility(View.VISIBLE);

    }
  }

  //Loader callbacks

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, currentItemUri, projection, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {

    ViewTreeObserver viewTreeObserver = imageContainer.getViewTreeObserver();
    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        imageContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        imageContainer.setImageBitmap(ImageUtils.getBitmapFromUri(
                Uri.parse(data.getString(data.getColumnIndexOrThrow(ItemEntry.COLUMN_IMAGE))),
                imageContainer,
                EditorActivity.this));
        currentItemImageUri = Uri.parse(
                data.getString(data.getColumnIndexOrThrow(ItemEntry.COLUMN_IMAGE)));
      }
    });
    if (data.moveToFirst()) {
      productNameEditText.setText(data.getString(
              data.getColumnIndexOrThrow(ItemEntry.COLUMN_PRODUCT_NAME)));
      quantityEditText.setText(Integer.toString(data.getInt(
              data.getColumnIndexOrThrow(ItemEntry.COLUMN_QUANTITY))));
      listPriceEditText.setText(Integer.toString(data.getInt(
              data.getColumnIndexOrThrow(ItemEntry.COLUMN_PRICE))));
      supplierEmailEditText.setText(data.getString(
              data.getColumnIndexOrThrow(ItemEntry.COLUMN_SUPPLIER_EMAIL)));
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    productNameEditText.setText("");
    quantityEditText.setText("");
    listPriceEditText.setText("");
    supplierEmailEditText.setText("");
  }

  // Menu item Setup

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_editor, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    if (currentItemUri == null) {
      MenuItem menuItem = menu.findItem(R.id.editor_menu_delete);
      menuItem.setVisible(false);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.editor_menu_save:
        saveItem();
        return true;
      case R.id.editor_menu_delete:
        showDeleteConfirmationDialog();
        return true;
      case android.R.id.home:
        if (!currentItemHasChanged) {
          NavUtils.navigateUpFromSameTask(EditorActivity.this);
          return true;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            NavUtils.navigateUpFromSameTask(EditorActivity.this);
          }
        };
        unsavedChangesDialog(discardButtonClickListener);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // check we've got the right request code
    if (requestCode == imageRequestCode) {
      // check we've got an ok resultCode
      if (resultCode == Activity.RESULT_OK) {
        // check there's data for us to handle:
        if (data != null) {
          // grantUriPermission(getPackageName(), data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
          currentItemImageUri = data.getData();
          if (Build.VERSION.SDK_INT >= 19) {
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(currentItemImageUri, takeFlags);
          }
          imageContainer.setImageBitmap(ImageUtils.getBitmapFromUri(
                  currentItemImageUri, imageContainer, this));
        }
      }
    }
  }

  // Change functions - save, delete

  private void saveItem() {
    String productName = productNameEditText.getText().toString().trim();
    String quantityString = quantityEditText.getText().toString().trim();
    String priceString = listPriceEditText.getText().toString().trim();
    String supplierEmail = supplierEmailEditText.getText().toString().trim();
    String imageUriString = null;
    if (currentItemImageUri != null) {
      imageUriString = currentItemImageUri.toString();
    }

    // Validation - we require a product name and supplier email at minimum

    if (TextUtils.isEmpty(productName)
            || TextUtils.isEmpty(supplierEmail) || imageUriString == null) {
      Toast.makeText(this, "New products require a name, supplier email, and image",
              Toast.LENGTH_SHORT).show();
      return;
    }

    // Set default quantity of 0 if empty
    if (TextUtils.isEmpty(quantityString)) {
      quantityString = defaultQuantity;
    }
    int quantity = Integer.parseInt(quantityString);

    // set default price of 0 if empty
    if (TextUtils.isEmpty(priceString)) {
      priceString = defaultPrice;
    }
    int price = Integer.parseInt(priceString);

    ContentValues values = new ContentValues();
    values.put(ItemEntry.COLUMN_PRODUCT_NAME, productName);
    values.put(ItemEntry.COLUMN_QUANTITY, quantity);
    values.put(ItemEntry.COLUMN_PRICE, price);
    values.put(ItemEntry.COLUMN_SUPPLIER_EMAIL, supplierEmail);
    values.put(ItemEntry.COLUMN_IMAGE, imageUriString);

    if (currentItemUri == null) {
      Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);
      if (newUri == null) {
        Toast.makeText(this, error_save_new, Toast.LENGTH_SHORT).show();
      }
      else {
        Toast.makeText(this, success_save_new, Toast.LENGTH_SHORT).show();
        finish();
      }
    }
    else {
      int rowsChanged = getContentResolver().update(currentItemUri, values, null, null);
      if (rowsChanged == 0) {
        Toast.makeText(this, error_update, Toast.LENGTH_SHORT).show();
      }
      else {
        Toast.makeText(this, success_update, Toast.LENGTH_SHORT).show();
        finish();
      }
    }
  }

  public void deleteItem() {
    int rowsDeleted = getContentResolver().delete(currentItemUri, null, null);
    if (rowsDeleted == 0) {
      Toast.makeText(this, error_delete, Toast.LENGTH_SHORT).show();
    }
    else {
      Toast.makeText(this, success_delete, Toast.LENGTH_SHORT).show();
    }
    finish();
  }

  public void incrementQuantity() {
    String currQuantity = quantityEditText.getText().toString();
    int intQuantity = Integer.parseInt(currQuantity);
    if (currQuantity != null) {
      intQuantity += 1;
      quantityEditText.setText(String.valueOf(intQuantity));
      return;
    }
    quantityEditText.setText("1");
  }

  private void decrementQuantity() {
    String currQuantity = quantityEditText.getText().toString();
    int intQuantity = Integer.parseInt(currQuantity);
    if (intQuantity > 0 && currQuantity != null) {
      intQuantity -= 1;
      quantityEditText.setText(String.valueOf(intQuantity));
      return;
    }
    quantityEditText.setText("0");
  }
  // Unsaved changes/Deletion dialog boxes

  private void unsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(unsaved_changes);
    builder.setPositiveButton(unsaved_changes_discard, discardButtonClickListener);
    builder.setNegativeButton(keep_editing, new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int id) {
        if (dialog != null) {
          dialog.dismiss();
        }
      }
    });

    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  private void showDeleteConfirmationDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.editor_ask_delete);
    builder.setPositiveButton(delete_button, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        deleteItem();
      }
    });
    builder.setNegativeButton(cancel_button, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (dialog != null) {
          dialog.dismiss();
        }
      }
    });

    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  @Override
  public void onBackPressed() {
    if (!currentItemHasChanged){
      super.onBackPressed();
      return;
    }

    DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    };
    unsavedChangesDialog(discardButtonClickListener);
  }
}
