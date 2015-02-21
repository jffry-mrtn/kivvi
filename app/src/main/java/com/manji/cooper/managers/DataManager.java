package com.manji.cooper.managers;

import android.util.Log;

import com.manji.cooper.R;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.CSVParser;
import com.manji.cooper.custom.Resource;
import com.manji.cooper.utils.Utility;

import java.util.HashMap;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class DataManager {

    private static DataManager instance;

    private HashMap<String, CSVData> data;
    private CSVParser csvParser;

    private String[] resources;

    private int dataRetrieved = 0;

    private Resource.OnRetrievedListener csvRetrievedListn = new Resource.OnRetrievedListener() {
        @Override
        public void onRetrieved(Resource resource) {
            Log.d("CSV response", resource.getContent());
            CSVData csvData = csvParser.parseCSV(resource.getContent());

            data.put(csvData.hashCode()+"", csvData);
            dataRetrieved++;

            if (isDone()){
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

        resources = new String[] {
            Utility.activity.getResources().getString(R.string.nutrient_value_2008_eggs)};

        initDataSets();

    }

    private void initDataSets(){
        ResourceHandler rh = ResourceHandler.getInstance();

        //fetch all data sets
        for (String s: resources){
            rh.getResource(s, csvRetrievedListn);
        }
    }

    private boolean isDone(){
        return dataRetrieved == resources.length;
    }
}
