package com.manji.cooper.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manji.cooper.R;
import com.manji.cooper.model.Food;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Food> content;

    public HistoryAdapter(Context context, ArrayList<Food> content) {
        this.context = context;
        this.content = content;
    }

    @Override
    public int getCount() {
        return (content == null) ? 0: content.size();
    }

    @Override
    public Food getItem(int i) {
        return content.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View drawerRow = (View) inflater.inflate(R.layout.history_item, viewGroup, false);

        TextView drawerTitle = (TextView) drawerRow.findViewById(R.id.history_item_title);
        TextView drawerTimestamp = (TextView) drawerRow.findViewById(R.id.history_item_timestamp);

        drawerTitle.setText(getItem(i).getMealTitle().substring(0,1).toUpperCase() + getItem(i).getMealTitle().substring(1));

        String formattedDate = DateUtils.formatDateTime(context, getItem(i).getTimestamp().getTime(),
                (DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NUMERIC_DATE));

        drawerTimestamp.setText(formattedDate);
        return drawerRow;
    }

}
