package com.manji.cooper.custom;

import android.os.AsyncTask;
import android.util.Log;

import com.manji.cooper.listeners.OnResourceFetchedListener;
import com.manji.cooper.utils.Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class FetchResourceAsyncTask extends AsyncTask {

    private final String TAG = FetchResourceAsyncTask.class.getSimpleName();

    private String url = null;
    private OnResourceFetchedListener listener = null;

    public FetchResourceAsyncTask(String url, OnResourceFetchedListener listn){
        this.url = url;
        listener = listn;
    }

    @Override
    protected String doInBackground(Object[] params) {

        Log.d(TAG, "Fetching resource from url: " + url);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        InputStream inputStream = null;

        StringBuilder sb = new StringBuilder();

        //retrieve resource from url
        try{
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 8);

            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            inputStream.close();

            final String res = sb.toString();

            Utility.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onSuccess(res);
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
