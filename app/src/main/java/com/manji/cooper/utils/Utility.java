package com.manji.cooper.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class Utility {

    private static final String TAG = Utility.class.getSimpleName();

    public static Activity activity;
    public static final String OUTPAN_REQUEST_URL = "http://www.outpan.com/api/get-product.php?apikey=API_KEY&barcode=BAR_CODE";

    /**
     * Return width of screen
     * @return
     */
    public static int getScreenWidth(){
        if (activity == null)
            return -1;

        Display display = ((WindowManager)Utility.activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    /**
     * Return  height of screen
     * @return
     */
    public static int getScreenHeight() {
        if (activity == null)
            return -1;

        Display display = ((WindowManager)Utility.activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.y;
    }

}
