package com.manji.cooper.custom;

import com.manji.cooper.managers.DataManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by douglaspereira on 2015-02-21.
 */
public class Data implements Serializable {
    public HashMap<Integer, CSVData> csvData;
    public HashMap<String, ItemInfo> csvItems;


}
