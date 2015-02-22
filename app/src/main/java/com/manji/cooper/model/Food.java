package com.manji.cooper.model;

import com.manji.cooper.custom.CSVData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jeff on 15-02-21.
 */
public class Food implements Serializable {

    private int key;
    private String mealTitle;
    private CSVData dataSet;
    private Date timestamp;

    public Food(int key, String mealTitle, ArrayList<String> values, CSVData dataSet) {
        this.key = key;
        this.mealTitle = mealTitle;
        this.dataSet = dataSet;
    }

    public CSVData getDataSet() {
        return dataSet;
    }

    public String getMealTitle() {
        return mealTitle;
    }

    public int getKey() {
        return key;
    }
}
