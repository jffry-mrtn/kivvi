package com.manji.cooper.custom;

import android.os.AsyncTask;
import android.util.Log;

import com.manji.cooper.listeners.OnResourceFetchedListener;
import com.manji.cooper.utils.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class FetchResourceAsyncTask extends AsyncTask {

    private final String TAG = FetchResourceAsyncTask.class.getSimpleName();

    private String path = null;
    private OnResourceFetchedListener listener = null;

    private int CONNECTION_TIMEOUT = 10000;

    public FetchResourceAsyncTask(String path, OnResourceFetchedListener listn){
        this.path = path;
        listener = listn;
    }

    @Override
    protected String doInBackground(Object[] params) {

        Log.d(TAG, "Fetching resource from url: " + path);

        InputStream inputStream = null;

        StringBuilder sb = new StringBuilder();

        //retrieve resource from url
        try{
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 8);

            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            inputStream.close();

            final String res = sb.toString();
            final String key = url.hashCode() + "";

            Utility.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onSuccess(key, res);
                }
            });

        } catch (IOException e) {
            listener.onError(e.toString());
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() { super.onCancelled(); }

}
