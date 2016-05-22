package com.example.link.photogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Link on 13-May-16.
 */
public class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private BitmapCache bitmapCache = null;
    private String imagePath = "";

    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        imagePath = imageView.getTag().toString();
    }

    public void setBitmapCache(BitmapCache cache) { bitmapCache = cache; }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Object... params) {
        int size = (Integer)params[0];

        Bitmap bm = ImageDecoder.decodeSampledBitmapFromPath(imagePath, size, size);

        if (bitmapCache != null)
        {
            bitmapCache.addBitmapToMemoryCache(imagePath, bm);
        }

        return bm;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = imageViewReference.get();

        if (imageView == null || !imageView.getTag().toString().equals(imagePath)) {
               /* The path is not same. This means that this
                  image view is handled by some other async task.
                  We don't do anything and return. */
            return;
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    static public void loadBitmap(String path, ImageView imageView, int size) {
        imageView.setTag(path);
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(size);
    }

    static public void loadBitmap(BitmapCache bitmapCache, String path, ImageView imageView, int size) {
        imageView.setTag(path);
        Bitmap bitmap = bitmapCache.getBitmapFromMemCache(path);

        if (bitmap != null)
        {
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            task.setBitmapCache(bitmapCache);
            task.execute(size);
        }
    }
}