package com.harrisonseitz.shopkeeper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by harrisonseitz on 8/14/17.
 */

public class ImageUtils {

  public static String LOG_TAG = ImageUtils.class.getSimpleName();

   /* Helper method obtained from Carlos Andrés Jiménez on Github (https://github.com/crlsndrsjmnz)
     https://github.com/crlsndrsjmnz/MyShareImageExample/blob/master/app/src/main/java/co/carlosandresjimenez/android/myshareimageexample/MainActivity.java#L158
    */

  public static Bitmap getBitmapFromUri(Uri uri, ImageView imageContainer, Context context) {

    if (uri == null || uri.toString().isEmpty())
      return null;

    // Get the dimensions of the View
    int targetW = imageContainer.getWidth();
    int targetH = imageContainer.getHeight();

    InputStream input = null;
    try {
      input = context.getContentResolver().openInputStream(uri);

      // Get the dimensions of the bitmap
      BitmapFactory.Options bmOptions = new BitmapFactory.Options();
      bmOptions.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(input, null, bmOptions);
      input.close();

      int photoW = bmOptions.outWidth;
      int photoH = bmOptions.outHeight;

      // Determine how much to scale down the image
      int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

      // Decode the image file into a Bitmap sized to fill the View
      bmOptions.inJustDecodeBounds = false;
      bmOptions.inSampleSize = scaleFactor;
      bmOptions.inPurgeable = true;

      input = context.getContentResolver().openInputStream(uri);
      Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
      input.close();
      return bitmap;

    } catch (FileNotFoundException fne) {
      Log.e(LOG_TAG, "Failed to load image.", fne);
      return null;
    } catch (Exception e) {
      Log.e(LOG_TAG, "Failed to load image.", e);
      return null;
    } finally {
      try {
        input.close();
      } catch (IOException ioe) {

      }
    }
  }
}
