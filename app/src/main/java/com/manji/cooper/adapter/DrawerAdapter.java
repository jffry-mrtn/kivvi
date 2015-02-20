package com.manji.cooper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.manji.cooper.R;

public class DrawerAdapter extends BaseAdapter {

    private Context context;
    private String[] content;

    public DrawerAdapter(Context context, String[] content) {
        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.length;
    }

    @Override
    public String getItem(int i) {
        return content[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View drawerRow = inflater.inflate(R.layout.drawer_item, viewGroup, false);
        return drawerRow;
    }

}
