package com.manji.cooper.managers;

import android.util.Log;

import com.manji.cooper.R;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.CSVParser;
import com.manji.cooper.custom.Resource;
import com.manji.cooper.listeners.OnDataRetrievedListener;
import com.manji.cooper.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class DataManager {

    private final String TAG = DataManager.class.getSimpleName();

    private static DataManager instance;

    private HashMap<Integer, CSVData> data;
    private HashMap<String, ItemInfo> items;

    private CSVParser csvParser;

    private String[] resources;

    private int dataRetrieved = 0;

    private OnDataRetrievedListener onDataRetrievedListener;

    public class ItemInfo {
        public int csvKey;
        public ArrayList<String> values;

        ItemInfo (int k, ArrayList<String> a){
            csvKey = k;
            values = a;
        }
    }

    private Resource.OnRetrievedListener csvRetrievedListn = new Resource.OnRetrievedListener() {
        @Override
        public void onRetrieved(Resource resource) {
            CSVData csvData = csvParser.parseCSV(resource.getContent());

            Log.d(TAG, "Parsed csv: " + csvData);

            int csvKey = csvData.hashCode();

            data.put(csvKey, csvData);

            for (String k: csvData.getKeys()){
                items.put(k, new ItemInfo(csvKey, csvData.getEntry(k)));
            }

            dataRetrieved++;

            if (isDone()){
                Log.d(TAG, "Retrieved all CSV resources: " + data.size());

                onDataRetrievedListener.onDataRetrieved();
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
        data = new HashMap<>();
        items = new HashMap<>();

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

    public void fetchData(){
        ResourceHandler rh = ResourceHandler.getInstance();

        Log.d(TAG, "Fetching CSV resources");

        for (String s: resources){
            rh.getResource(s, csvRetrievedListn);
        }
    }

    public HashMap<String, ItemInfo> getFilteredData(String filter){
        if (filter.trim().isEmpty())
            return items;
        else{
            HashMap<String, ItemInfo> filteredResults = new HashMap<>();

            for (String i: items.keySet()){
                if (i.contains(filter)){
                    filteredResults.put(i, items.get(i));
                }
            }

            return filteredResults;
        }
    }

    public HashMap<String, ItemInfo> getItems(){
       return items;
    }

    public HashMap<Integer, CSVData> getData() {
        return data;
    }

    private boolean isDone(){
        return dataRetrieved == resources.length;
    }

}
