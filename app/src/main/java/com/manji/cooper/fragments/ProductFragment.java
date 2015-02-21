package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.manji.cooper.MainActivity;
import com.manji.cooper.R;
import com.manji.cooper.adapter.ProductAdapter;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.managers.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductFragment extends Fragment {

    private ArrayList<CSVData> data;
    private ArrayList<String> itemNames;

    private Context context;
    private View layoutView;

    private ListView productListView;
    private EditText enterProductEditText;
    private ProductAdapter productAdapter;
    private TextView hintTextView;

    private HashMap<String, ItemInfo> filteredResults = null;
    private boolean filteredFromBarcode = false;

    public ProductFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_product, container, false);
        context = getActivity().getApplicationContext();

        hintTextView = (TextView) layoutView.findViewById(R.id.product_search_hint);
        enterProductEditText = (EditText) layoutView.findViewById(R.id.enter_product_edittext);
        productListView = (ListView) layoutView.findViewById(R.id.product_listview);

        productAdapter = new ProductAdapter(context);
        productListView.setAdapter(productAdapter);

        productListView.setOnItemClickListener(new productListViewOnClickListener());

        // Text Watcher for the Filterable
        enterProductEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    hintTextView.setVisibility(View.INVISIBLE);
                } else {
                    hintTextView.setVisibility(View.VISIBLE);
                }

                productAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (filteredResults != null && !filteredResults.isEmpty()){
            productAdapter.setFilteredResults(filteredResults);
            hintTextView.setVisibility(View.GONE);
        }

        return layoutView;
    }

    public void setData(ArrayList<CSVData> csvData, ArrayList<String> names) {
        this.data = csvData;
        this.itemNames = names;

        if (productListView != null) {
            productAdapter = new ProductAdapter(context);
            productListView.setAdapter(productAdapter);
            productAdapter.notifyDataSetChanged();
        }
    }

    /** Search **/
    public void search(CharSequence query) {
        if (query.length() > 0) {
            productAdapter.getFilter().filter(query);
        }
    }

    public void clearSearchFilter() {
        productAdapter.getFilter().filter("");

        productAdapter = new ProductAdapter(context);
        productListView.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    public void clearItemSelection() {
        productAdapter.notifyDataSetChanged();
    }

    private class productListViewOnClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String clickedItem = productAdapter.getItem(position);

            int csvKey = productAdapter.getFilteredHashMap().get(clickedItem).csvKey;
            ArrayList<String> values = productAdapter.getFilteredHashMap().get(clickedItem).values;
            CSVData dataSet = DataManager.getInstance().getData().get(csvKey);

            ((MainActivity) getActivity()).showNutritionFragment(clickedItem, values, dataSet);
        }
    }

    /* Set filtered results retrieved from barcode scan
     *
     */
    public void setFilteredResults(HashMap<String, ItemInfo> fr){
        filteredResults = fr;
        filteredFromBarcode = true;
    }
}
