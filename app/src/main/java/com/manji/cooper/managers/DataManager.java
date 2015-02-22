package com.manji.cooper.managers;

import android.util.Log;

import com.manji.cooper.R;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.CSVParser;
import com.manji.cooper.custom.Data;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.custom.Resource;
import com.manji.cooper.listeners.OnDataRetrievedListener;
import com.manji.cooper.utils.LocalStorage;
import com.manji.cooper.utils.Utility;

import java.util.HashMap;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class DataManager{

    private final String TAG = DataManager.class.getSimpleName();

    private static DataManager instance;

    private Data data;

    private CSVParser csvParser;

    private String[] resources;

    private int dataRetrieved = 0;

    private OnDataRetrievedListener onDataRetrievedListener;

    private LocalStorage storage;


    private Resource.OnRetrievedListener csvRetrievedListn = new Resource.OnRetrievedListener() {
        @Override
        public void onRetrieved(Resource resource) {
            CSVData csvData = csvParser.parseCSV(resource.getContent());

            Log.d(TAG, "Parsed csv: " + csvData);

            int csvKey = csvData.hashCode();

            data.csvData.put(csvKey, csvData);

            for (String k: csvData.getKeys()){
                data.csvItems.put(k, new ItemInfo(csvKey, csvData.getEntry(k)));
            }

            dataRetrieved++;

            //Store csv locally
            storage.setString(resource.getKey(), csvData.getStringContent());

            if (isReady()){
                Log.d(TAG, "Retrieved all CSV resources: " + data.csvData.size());

                storage.commit();

                onDataRetrievedListener.onDataRetrieved();
            }
        }

        @Override
        public void onError(String error) {
            Log.e("CSV error", error);
            onDataRetrievedListener.onError(error);
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
        data = new Data();

        storage = new LocalStorage(LocalStorage.STORAGE_KEY);

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

        data.csvData = new HashMap<>();
        data.csvItems = new HashMap<>();

        ResourceHandler rh = ResourceHandler.getInstance();

        Log.d(TAG, "Fetching CSV resources");

        for (String s: resources){
            String key = s.hashCode()+"";
            if (storage.containsKey(key)){
                String csv = storage.getString(key);
                rh.setOnRetrievedListener(csvRetrievedListn);
                rh.onSuccess(key, csv);

            }else{
                rh.getResource(s, csvRetrievedListn);
            }

        }
    }

    public HashMap<String, ItemInfo> getFilteredData(String filter){
        if (filter.trim().isEmpty())
            return data.csvItems;
        else{
            HashMap<String, ItemInfo> filteredResults = new HashMap<>();

            for (String i: data.csvItems.keySet()){
                if (i.contains(filter)){
                    filteredResults.put(i, data.csvItems.get(i));
                }
            }

            return filteredResults;
        }
    }

    public HashMap<String, ItemInfo> getItems(){
       return data.csvItems;
    }

    public HashMap<Integer, CSVData> getData() {
        return data.csvData;
    }

    public boolean isReady(){
        return dataRetrieved == resources.length;
    }

}
