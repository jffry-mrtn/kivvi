package com.manji.cooper.managers;

import android.util.Log;

import com.manji.cooper.R;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.CSVParser;
import com.manji.cooper.custom.Resource;
import com.manji.cooper.listeners.OnDataRetrievedListener;
import com.manji.cooper.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class DataManager {

    private final String TAG = DataManager.class.getSimpleName();

    private static DataManager instance;

    private ArrayList<CSVData> data;
    private CSVParser csvParser;
    private ArrayList<String> itemNames;

    private String[] resources;

    private int dataRetrieved = 0;

    private OnDataRetrievedListener onDataRetrievedListener;

    private Resource.OnRetrievedListener csvRetrievedListn = new Resource.OnRetrievedListener() {
        @Override
        public void onRetrieved(Resource resource) {
            CSVData csvData = csvParser.parseCSV(resource.getContent());

            Log.d(TAG, "Parsed csv: " + csvData.getClass());

            data.add(csvData);
            itemNames.addAll(csvData.getKeys());

            dataRetrieved++;

            if (isDone()){
                Log.d(TAG, "Retrieved all CSV resources: " + data.size());

                onDataRetrievedListener.onDataRetrieved(data, itemNames);
                //Callback or notify that all set have been retrieved
            }
        }

        @Override
        public void onError(String error) {
            Log.e("CSV error", error);
        }
    };

    public static DataManager getInstance(){
        if (instance == null){
            instance = new DataManager();
        }

        return instance;
    }

    private DataManager(){
        csvParser = new CSVParser();
        data = new ArrayList<>();
        itemNames = new ArrayList<>();

        resources = new String[] {
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_eggs),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_baked_goods),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_beverages),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_grains),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_dairy),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_fastfood),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_fats),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_fish),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_fruits),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_meats),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_mixed_dishes),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_misc),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_nuts_legumes),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_soups),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_snacks),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_sugars),
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_vegetables),
        };
    }

    public void setOnDataRetrievedListener(OnDataRetrievedListener listn){
        onDataRetrievedListener = listn;
    }

    public void getData(){
        ResourceHandler rh = ResourceHandler.getInstance();

        Log.d(TAG, "Fetching CSV resources");

        for (String s: resources){
            rh.getResource(s, csvRetrievedListn);
        }
    }

    private boolean isDone(){
        return dataRetrieved == resources.length;
    }

}
