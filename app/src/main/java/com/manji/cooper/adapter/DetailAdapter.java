package com.manji.cooper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manji.cooper.R;

import java.util.ArrayList;

public class DetailAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> content;

    public DetailAdapter(Context context, ArrayList<String> content) {
        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public String getItem(int i) {
        return content.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView drawerRow = (TextView) inflater.inflate(R.layout.detail_item, viewGroup, false);
        drawerRow.setText(getItem(i));
        return drawerRow;
    }

}
