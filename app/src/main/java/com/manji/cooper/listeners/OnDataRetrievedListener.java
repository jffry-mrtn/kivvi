package com.manji.cooper.listeners;

/**
 * Created by douglaspereira on 2015-02-21.
 */
public interface OnDataRetrievedListener {
    public void onDataRetrieved();
    public void onRetrievalStarted();
    public void onError(String error);
}
