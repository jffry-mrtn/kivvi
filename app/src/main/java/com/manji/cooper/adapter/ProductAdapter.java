package com.manji.cooper.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.manji.cooper.R;
import com.manji.cooper.custom.CSVData;

public class ProductAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<CSVData> data;
    private ArrayList<CSVData> filteredData;

    public ProductAdapter(Context context, ArrayList<CSVData> content) {
        this.context = context;
        this.data = content;
        this.filteredData = data;
    }

    @Override
    public int getCount() {
        if (filteredData != null) {
            return filteredData.size();
        }

        return 0;
    }

    @Override
    public CSVData getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.product_item, viewGroup, false);
        TextView productTitle = (TextView) row.findViewById(R.id.product_title);

        productTitle.setText(getItem(i).toString());
        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults searchResults = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    searchResults.values = data;
                    searchResults.count = data.size();
                } else {
                    ArrayList<CSVData> searchResultsData = new ArrayList<CSVData>();

                    for (CSVData item : data) {
                        if (item.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            searchResultsData.add(item);
                        }
                    }

                    searchResults.values = searchResultsData;
                    searchResults.count = searchResultsData.size();
                }
                return searchResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<CSVData>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}