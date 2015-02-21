package com.manji.cooper.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.managers.DataManager;

import java.util.HashMap;

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

    public static HashMap<Integer, CSVData> getDataFromStorage(LocalStorage storage){
        boolean isStored = storage.containsKey(LocalStorage.DATA_SETS_TAG);

        HashMap<Integer, CSVData> data = null;

        try{
            if (isStored)
                data = (HashMap<Integer, CSVData>)storage.retrieveObject(LocalStorage.DATA_SETS_TAG);

        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            return null;
        }

        return data;
    }

    public static HashMap<String, ItemInfo> getItemsFromStorage(LocalStorage storage){
        boolean isStored = storage.containsKey(LocalStorage.ALL_ITEMS_TAG);

        HashMap<String, ItemInfo> data = null;

        try{
            if (isStored)
                data = (HashMap<String, ItemInfo>)storage.retrieveObject(LocalStorage.ALL_ITEMS_TAG);

        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            return null;
        }

        return data;
    }



}
