package com.manji.cooper.custom;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemInfo implements Serializable {
    public int csvKey;
    public ArrayList<String> values;

    public ItemInfo (int k, ArrayList<String> a){
        csvKey = k;
        values = a;
    }
}