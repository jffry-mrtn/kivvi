package com.manji.cooper.custom;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by douglaspereira on 2015-02-21.
 */
public class Data implements Serializable {
    public HashMap<Integer, CSVData> csvData;
    public HashMap<String, ItemInfo> csvItems;


}
