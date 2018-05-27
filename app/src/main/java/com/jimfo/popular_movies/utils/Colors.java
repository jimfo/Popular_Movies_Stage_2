package com.jimfo.popular_movies.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Colors {

    public static int getDominantColor(Bitmap bitmap) {
        // https://stackoverflow.com/questions/8471236/finding-the-dominant-color-of-an-image-in-an-android-drawable/28145358

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();

        // Darken the color
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.7f;

        return Color.HSVToColor(hsv);
    }
}
