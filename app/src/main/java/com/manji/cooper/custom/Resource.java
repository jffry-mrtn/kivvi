package com.manji.cooper.custom;

/**
 * Created by douglaspereira on 2015-02-20.
 */
public class Resource{

    private final String TAG = Resource.class.getSimpleName();

    private String content = "";
    private Type type = Type.OTHER;
    private String key = "";

    public static interface OnRetrievedListener{
        public void onRetrieved(Resource resource);
        public void onError(String error);
    }

    public enum Type {
        XML, JSON, OTHER
    }

    public Resource(){

    }

    public Resource(String k, String res, Type t){
        super();

        key = k;
        content = res;
        type = t;
    }

    public String getContent(){
        return content;
    }

    public Type getType(){
        return type;
    }

    public String getKey(){
        return key;
    }
}
