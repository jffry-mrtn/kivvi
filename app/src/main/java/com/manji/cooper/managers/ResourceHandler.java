package com.manji.cooper.managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.parsers.DocumentBuilderFactory;

import com.manji.cooper.custom.FetchResourceAsyncTask;
import com.manji.cooper.custom.Resource;
import com.manji.cooper.listeners.OnResourceFetchedListener;

/**
 * Created by douglaspereira on 2015-02-19.
 */
public class ResourceHandler implements OnResourceFetchedListener {

    private final String TAG = ResourceHandler.class.getSimpleName();

    private static ResourceHandler instance = null;

    private Resource.OnRetrievedListener listener;

    private ResourceHandler(){

    }

    public static ResourceHandler getInstance(){
        if (instance == null){
            instance = new ResourceHandler();
        }

        return instance;
    }

    public void getResource(String url, Resource.OnRetrievedListener listn){
        if (url == null || listn == null) return;

        listener = listn;

        FetchResourceAsyncTask task = new FetchResourceAsyncTask(url, this);
        task.execute();
    }

    @Override
    public void onSuccess(String response) {

        Resource.Type type = (isJSON(response) ? Resource.Type.JSON
                            : (isXML(response) ? Resource.Type.XML
                            : Resource.Type.OTHER));

        listener.onRetrieved(new Resource(response, type));
    }

    @Override
    public void onError(String error) {
        listener.onError(error);
    }

    private boolean isJSON(String response){
        try {
            new JSONObject(response);
        } catch (JSONException ex) {
            try {
                new JSONArray(response);
            } catch (JSONException ex1) {
                return false;
            }
        }

        return true;
    }

    private boolean isXML(String response){
        try{
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response);

        }catch (Exception ex){
            return false;
        }

       return true;
    }
}
