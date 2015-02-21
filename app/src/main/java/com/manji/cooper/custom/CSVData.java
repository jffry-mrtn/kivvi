
package com.manji.cooper.custom;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class CSVData implements Serializable{

    private Context context;
    private String stringCSV = null;

    private List<String> attributeNames = null;
    private List<String> attributeUnits = null;

    private HashMap<String, ArrayList<String>> data = null;

    public CSVData(Context c, HashMap h){
        context = c;

        data = h;
    }

    /*
     * Return this CSV object's list of attributes
     */
    public void setAttributeNames(List<String> n){
        List<String> names = new ArrayList<String>();
        for (String v : n)
            names.add(v.toLowerCase().trim());

        attributeNames = names;
    }

    /*
    * Return this CSV object's list of attributes
    */
    public void setAttributeUnits(List<String> u){
        List<String> units = new ArrayList<String>();
        for (String v : u)
            units.add(v.toLowerCase().trim());

        attributeUnits = units;
    }

    /*
     * Return this CSV object's list of attributes
     */
    public List<String> getAttributeNames(){
        return attributeNames;
    }

    /*
    * Return this CSV object's list of attributes
    */
    public List<String> getAttributeUnits(){
        return attributeUnits;
    }

    /*
     * Return the entry with the specified key
     */
    public ArrayList<String> getEntry(String key){
        return data.get(key.toLowerCase());
    }

    /*
     * Return the value of the specified attribute from the specified entry
     */
    public String getValue(String key, String attribute){
        if (attributeNames == null || attributeNames.indexOf(attribute) == -1) return "";

        return getEntry(key).get(attributeNames.indexOf(attribute));
    }

    public List<String> getKeys(){
        List<String> keys = new ArrayList();
        keys.addAll(data.keySet());

        return keys;
    }

//    @Override
//    public String toString() {
//        if (data == null || context == null) return null;
//
//        if (stringCSV == null){
//            stringCSV = "";
//            String n = null, u = null;
//
//            for (String k : data.keySet()){
//                if (k == context.getResources().getString(R.string.csv_attributes_names)){
//                    n = k;
//                }else if (k == context.getResources().getString(R.string.csv_attributes_units)){
//                    u = k;
//                }else{
//                    stringCSV = stringCSV + data.get(k) + "\n";
//                }
//            }
//
//            stringCSV = u + "\n" + stringCSV;
//            stringCSV = n + "\n" + stringCSV;
//
//        }
//
//        return stringCSV;
//    }
}
