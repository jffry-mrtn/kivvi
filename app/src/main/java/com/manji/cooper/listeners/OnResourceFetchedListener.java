package com.manji.cooper.listeners;

/**
 * Created by douglaspereira on 2015-02-07.
 */
public interface OnResourceFetchedListener {

    public void onSuccess(String resource);
    public void onError(String error);
}
