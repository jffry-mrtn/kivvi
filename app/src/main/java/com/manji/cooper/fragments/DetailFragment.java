package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.manji.cooper.MainActivity;
import com.manji.cooper.R;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.model.Constants;
import com.manji.cooper.model.Food;
import com.manji.cooper.utils.LocalStorage;
import com.manji.cooper.utils.Utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class DetailFragment extends Fragment {

    private ArrayList<CSVData> data;
    private Context context;
    private View layoutView;

    private TextView quantityLabel;
    private TextView foodTitleTextView;
    private LinearLayout nutritionLayout;
    private ArrayList<TextView> nutritionTextViewList;

    public DetailFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_nutrition, container, false);
        context = getActivity().getApplicationContext();

        nutritionTextViewList = new ArrayList<>();

        nutritionLayout = (LinearLayout) layoutView.findViewById(R.id.nutrition_layout);
        foodTitleTextView = (TextView) layoutView.findViewById(R.id.food_title);

        quantityLabel = (TextView) layoutView.findViewById(R.id.quantity_label);

//        doneButton = (Button) layoutView.findViewById(R.id.done_product);
        
//        doneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveFoodAndReset();
//            }
//        });

        return layoutView;
    }

    private void initData() {
    }

    private void generateNutritionViews() {
//        for (String attrName : Utility.getFormattedAttributeNames(food.getKey())) {
//            if ((!attrName.contains("FOOD NAME")) && (!attrName.contains("WEIGHT"))) {
//                String attrValue;
//
//                if (attrName.contains(" (")) {
//                    attrValue = food.getDataSet().getValue(food.getMealTitle(), attrName.substring(0, attrName.indexOf(" (")).toLowerCase());
//                } else {
//                    attrValue = food.getDataSet().getValue(food.getMealTitle(), attrName.toLowerCase());
//                }
//
                // If the attribute is empty, don't bother showing it
//                if (!attrValue.equalsIgnoreCase("")) {
//                    generateNutritionTextView(attrName, attrValue);
//                }
//            }
//        }
    }

    private void generateNutritionTextView(String attrName, String attrValue) {
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
        attributeValueTextView.setText(attrValue);
        attributeValueTextView.setTextColor(context.getResources().getColor(R.color.dark_grey));
        attributeValueTextView.setLayoutParams(lpValue);

        nutritionLayout.addView(attributeLabelTextView);
        nutritionLayout.addView(attributeValueTextView);
        nutritionTextViewList.add(attributeValueTextView);
    }

    public void setData(Food food) {
    }
}
