package com.jimfo.popular_movies.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;

public class GeneralUtils {

    /**
     * Purpose : To get the dominant color from an image and darken color.
     * @param bitmap : Backdrop poster image
     * @return : color
     */
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

    /**
     *
     * @param context : The context of the calling Activity
     * @return : int
     */
    private static int getPixels(Context context){

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (width * scale + 0.5f);
    }

    /**
     * Purpose : To set the height and width of an image based on orientation
     * @param context : The context of the calling Activity
     * @return : An int array
     */
    public static int[] getWxH(Context context){

        int[] wxh = new int[2];

        int pixels = getPixels(context);

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            wxh[0] = (pixels / 2);
            wxh[1] = (int) ((pixels / 2) * 1.5f);
        }
        else {
            wxh[0] = (pixels / 3);
            wxh[1] = (int) ((pixels / 3) * 1.5f);
        }
        return wxh;
    }

    /**
     * Purpose : To set the height and width of the movie poster image and backdrop poster image
     * @param context : The context of the calling Activity
     * @return : An int array
     */
    public static int[] getDAWxH(Context context){

        int[] WxH = new int[4];

        int pixels = getPixels(context);
        WxH[0] = (pixels / 3);
        WxH[1] = (int) ((pixels / 3) * 1.25f);
        WxH[2] = pixels;
        WxH[3] = (pixels / 2);

        return WxH;
    }
}
