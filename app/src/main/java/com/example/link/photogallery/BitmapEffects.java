package com.example.link.photogallery;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Link on 04-May-16.
 */
public class BitmapEffects {

    final static Matrix matrix = new Matrix();

    public static Bitmap rotateLeft(Bitmap bitmap) {
        Bitmap newBitmap;

        matrix.reset();

        matrix.postRotate(-90);

        newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return newBitmap;
    }

    public static Bitmap rotateRight(Bitmap bitmap) {
        Bitmap newBitmap;

        matrix.reset();

        matrix.postRotate(90);

        newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return newBitmap;
    }

    public static Bitmap flipHorizontally(Bitmap bitmap) {
        Bitmap newBitmap;

        matrix.reset();
        matrix.preScale(-1, 1);
        newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        return newBitmap;
    }

    public static Bitmap flipVertically(Bitmap bitmap) {
        Bitmap newBitmap;

        matrix.reset();
        matrix.preScale(1, -1);
        newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        return newBitmap;
    }

    public static Bitmap effectBW(Bitmap bitmap) {
        Bitmap bmpMonochrome = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bmpMonochrome;
    }

    public static Bitmap effectSepia(Bitmap bitmap) {
        Bitmap sepiaBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(sepiaBitmap);

        final ColorMatrix matrixA = new ColorMatrix();
        // making image B&W
        matrixA.setSaturation(0);

        final ColorMatrix matrixB = new ColorMatrix();
        // applying scales for RGB color values
        matrixB.setScale(1f, .95f, .82f, 1.0f);
        matrixA.setConcat(matrixB, matrixA);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrixA));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return sepiaBitmap;
    }

    public static Bitmap effectInvert(Bitmap bitmap) {
        float[] negative = {
                -1.0f,     0,     0,    0, 255, // red
                0, -1.0f,     0,    0, 255, // green
                0,     0, -1.0f,    0, 255, // blue
                0,     0,     0, 1.0f,   0  // alpha
        };

        Bitmap invertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(invertedBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(negative));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return invertedBitmap;
    }

    public static Bitmap effectPolaroid(Bitmap bitmap) {
        // Put some contrast first
        float[] contrast = new float[] {
                2, 0, 0, 0, -130,
                0, 2, 0, 0, -130,
                0, 0, 2, 0,	-130,
                0, 0, 0, 1, 0
        };

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Create an off-screen bitmap where the canvas content will be recorded
        Bitmap polaroidBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas polaroidCanvas = new Canvas(polaroidBitmap);
        // Draw an off-white color rectangle that will contain the photo

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(contrast);
        // Draw a very light shadow between the picture and the white margins
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setShadowLayer((int) (offset / 2), 0, 0, Color.BLACK);
        paint.setColorFilter(filter);
        // Draw the photo on the white rectangle
        polaroidCanvas.drawBitmap(bitmap, 0, 0, paint);

        return polaroidBitmap;
    }

    public static Bitmap tune(Bitmap bitmap, int brightness, float contrast) {
        contrast += 1.f;
        float[] tune = new float[]  {
                contrast, 0, 0, 0, brightness,
                0, contrast, 0, 0, brightness,
                0, 0, contrast, 0, brightness,
                0, 0, 0, 1, 0
        };
        Bitmap tunedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tunedBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(tune));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return tunedBitmap;
    }

    public static Bitmap tuneBrightness(Bitmap bitmap, int value) {
        float[] brightness = new float[]  {
            1, 0, 0, 0, value,
                    0, 1, 0, 0, value,
                    0, 0, 1, 0, value,
                    0, 0, 0, 1, 0   };
        Bitmap tunedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tunedBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(brightness));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return tunedBitmap;
    }

    public static Bitmap tuneContrast(Bitmap bitmap, float value) {
        float[] contrast = new float[]  {
                value, 0, 0, 0, 0,
                0, value, 0, 0, 0,
                0, 0, value, 0, 0,
                0, 0, 0, 1, 0 };
        Bitmap tunedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tunedBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(contrast));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return tunedBitmap;
    }
}
