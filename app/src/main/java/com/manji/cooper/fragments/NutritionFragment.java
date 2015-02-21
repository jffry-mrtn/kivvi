package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.manji.cooper.R;
import com.manji.cooper.adapter.ProductAdapter;
import com.manji.cooper.custom.CSVData;

import java.util.ArrayList;

public class NutritionFragment extends Fragment {

    private ArrayList<CSVData> data;
    private Context context;
    private View layoutView;

    private SeekBar quantitySeekbar;
    private TextView quantityLabel;
    private TextView foodTitleTextView;
    private TextView foodCaloriesTextView;

    private LinearLayout nutritionLayout;

    private String mealTitle;
    private ArrayList<String> nutritionalValues;
    private CSVData dataSet;

    public NutritionFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_nutrition, container, false);
        context = getActivity().getApplicationContext();

        nutritionLayout = (LinearLayout) layoutView.findViewById(R.id.nutrition_layout);
        foodTitleTextView = (TextView) layoutView.findViewById(R.id.food_title);
        foodCaloriesTextView = (TextView) layoutView.findViewById(R.id.food_calories);

        quantityLabel = (TextView) layoutView.findViewById(R.id.quantity_label);
        quantitySeekbar = (SeekBar) layoutView.findViewById(R.id.quantity_seekbar);
        
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

        initData();
        
        return layoutView;
    }

    private void initData() {
        generateNutritionViews();
        foodTitleTextView.setText(mealTitle.substring(0, 1).toUpperCase() + mealTitle.substring(1));

        // Set the calories
        foodCaloriesTextView.setText("281");
    }

    private void generateNutritionViews() {
        for (String attrName : dataSet.getAttributeNames()) {
            if (!attrName.equalsIgnoreCase("food name")) {
                String attrValue = dataSet.getValue(mealTitle, attrName);

                TextView attributeLabelTextView = new TextView(context);
                TextView attributeValueTextView = new TextView(context);

                LinearLayout.LayoutParams lpLabel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lpLabel.setMargins(0, 30, 0, 0);

                LinearLayout.LayoutParams lpValue = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lpValue.setMargins(0, 15, 0, 0);

                // Attribute Label
                attributeLabelTextView.setText(attrName);
                attributeLabelTextView.setTextColor(context.getResources().getColor(R.color.primary));
                attributeLabelTextView.setAllCaps(true);
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

    public void setData(String meal, ArrayList<String> values, CSVData dataSet) {
        this.mealTitle = meal;
        this.nutritionalValues = values;
        this.dataSet = dataSet;
    }
}
