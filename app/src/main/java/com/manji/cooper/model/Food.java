package com.manji.cooper.model;

import com.manji.cooper.custom.CSVData;

import java.io.Serializable;

import java.util.Date;

/**
 * Created by jeff on 15-02-21.
 */
public class Food implements Serializable {

    private int key;
    private int fid;
    private float factor = 1.0f;
    private String mealTitle;
    private CSVData dataSet;
    private Date timestamp;

    public Food(int key, String mealTitle, CSVData dataSet) {
        this.key = key;
        this.mealTitle = mealTitle;
        this.dataSet = dataSet;
        this.timestamp = new Date();
        this.fid = this.hashCode();
    }

    public CSVData getDataSet() {
        return dataSet;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMealTitle() {
        return mealTitle;
    }

    public int getKey() {
        return key;
    }

    public void setFactor(float f){
        factor = f;
    }

    public float getFactor(){
        return factor;
    }

    public int getFid() { return fid; }
}
