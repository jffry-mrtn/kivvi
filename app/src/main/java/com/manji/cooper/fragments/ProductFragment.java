package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.manji.cooper.MainActivity;
import com.manji.cooper.R;
import com.manji.cooper.adapter.ProductAdapter;
import com.manji.cooper.custom.CSVData;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    private ArrayList<CSVData> data;
    private Context context;
    private View layoutView;

    private Button getNutritionButton;
    private Button cancelButton;
    private ListView productListView;
    private EditText enterProductEditText;
    private ProductAdapter productAdapter;

    public ProductFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_product, container, false);
        context = getActivity().getApplicationContext();

        enterProductEditText = (EditText) layoutView.findViewById(R.id.enter_product_edittext);
        productListView = (ListView) layoutView.findViewById(R.id.product_listview);
        getNutritionButton = (Button) layoutView.findViewById(R.id.get_nutrition_button);
        cancelButton = (Button) layoutView.findViewById(R.id.cancel_button);

        productAdapter = new ProductAdapter(context, data);
        productListView.setAdapter(productAdapter);

        // Text Watcher for the Filterable
        enterProductEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getNutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNutritionFragment();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        return layoutView;
    }

    private void showNutritionFragment() {
        ((MainActivity) getActivity()).showNutritionFragment();
    }

    public void setData(ArrayList<CSVData> csvData) {
        this.data = csvData;
    }

    /** Search **/
    public void search(CharSequence query) {
        if (query.length() > 0) {
            productAdapter.getFilter().filter(query);
        }
    }

    public void clearSearchFilter() {
        productAdapter.getFilter().filter("");

        productAdapter = new ProductAdapter(context, data);
        productListView.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    public void clearItemSelection() {
        productAdapter.notifyDataSetChanged();
    }
}
