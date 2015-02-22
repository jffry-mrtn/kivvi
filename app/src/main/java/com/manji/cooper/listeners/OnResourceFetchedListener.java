package com.manji.cooper.listeners;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public interface OnResourceFetchedListener {

    public void onSuccess(String key, String resource);
    public void onError(String error);
}
