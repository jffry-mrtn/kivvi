package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.manji.cooper.R;
import com.manji.cooper.adapter.ProductAdapter;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.model.Food;
import com.manji.cooper.utils.Utility;

import java.util.ArrayList;

public class NutritionFragment extends Fragment {

    private ArrayList<CSVData> data;
    private Context context;
    private View layoutView;

    private SeekBar quantitySeekbar;
    private TextView quantityLabel;
    private TextView foodTitleTextView;

    private LinearLayout nutritionLayout;

    private Button saveButton;
    private Food food;

    public NutritionFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_nutrition, container, false);
        context = getActivity().getApplicationContext();

        nutritionLayout = (LinearLayout) layoutView.findViewById(R.id.nutrition_layout);
        foodTitleTextView = (TextView) layoutView.findViewById(R.id.food_title);

        quantityLabel = (TextView) layoutView.findViewById(R.id.quantity_label);
        quantitySeekbar = (SeekBar) layoutView.findViewById(R.id.quantity_seekbar);

        saveButton = (Button) layoutView.findViewById(R.id.save_product);
        
        quantitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quantityLabel.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });

        initData();
        
        return layoutView;
    }

    private void saveProduct() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        String json = gson.toJson(food, Food.class);
        Log.d("WEUWOSAJKHJWEYWOIEUWQOIEQW", json);
    }

    private void initData() {
        generateNutritionViews();
        int weight = Integer.parseInt(food.getDataSet().getValue(food.getMealTitle(), "weight"));

        quantityLabel.setText("" + weight);
        quantitySeekbar.setProgress(weight);
        quantitySeekbar.setMax((int) Math.floor(weight * 2));

        foodTitleTextView.setText(food.getMealTitle().substring(0, 1).toUpperCase() + food.getMealTitle().substring(1));
    }

    private void generateNutritionViews() {
        for (String attrName : Utility.getFormattedAttributeNames(food.getKey())) {
            if (!attrName.contains("FOOD NAME") || !attrName.contains("WEIGHT")) {
                String attrValue;

                if (attrName.contains(" (")) {
                    attrValue = food.getDataSet().getValue(food.getMealTitle(), attrName.substring(0, attrName.indexOf(" (")).toLowerCase());
                } else {
                    attrValue = food.getDataSet().getValue(food.getMealTitle(), attrName.toLowerCase());
                }

                TextView attributeLabelTextView = new TextView(context);
                TextView attributeValueTextView = new TextView(context);

                LinearLayout.LayoutParams lpLabel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lpLabel.setMargins(0, 30, 0, 0);

                LinearLayout.LayoutParams lpValue = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lpValue.setMargins(0, 15, 0, 0);

                // Attribute Label
                attributeLabelTextView.setText(attrName);
                attributeLabelTextView.setTextColor(context.getResources().getColor(R.color.primary));
                attributeLabelTextView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
                attributeLabelTextView.setLayoutParams(lpLabel);

                // Attribute Value
                attributeValueTextView.setText(attrValue.equalsIgnoreCase("") ? "N/A" : attrValue);
                attributeValueTextView.setTextColor(context.getResources().getColor(R.color.dark_grey));
                attributeValueTextView.setLayoutParams(lpValue);

                nutritionLayout.addView(attributeLabelTextView);
                nutritionLayout.addView(attributeValueTextView);
            }
        }
    }

    public void setData(Food food) {
        this.food = food;
    }
}
