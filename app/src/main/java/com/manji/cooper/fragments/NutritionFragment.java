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

public class NutritionFragment extends Fragment {

    private ArrayList<CSVData> data;
    private Context context;
    private View layoutView;

    private SeekBar quantitySeekbar;
    private TextView quantityLabel;
    private TextView foodTitleTextView;
    private LinearLayout nutritionLayout;
    private ArrayList<TextView> nutritionTextViewList;

    private Button doneButton;
    private Food food;
    private int foodWeight;
    private ArrayList<Float> nutritionValueList;
    private boolean isEditable;

    public NutritionFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_nutrition, container, false);
        context = getActivity().getApplicationContext();

        nutritionTextViewList = new ArrayList<>();
        nutritionValueList = new ArrayList<>();

        nutritionLayout = (LinearLayout) layoutView.findViewById(R.id.nutrition_layout);
        foodTitleTextView = (TextView) layoutView.findViewById(R.id.food_title);

        quantityLabel = (TextView) layoutView.findViewById(R.id.quantity_label);
        quantitySeekbar = (SeekBar) layoutView.findViewById(R.id.quantity_seekbar);

        doneButton = (Button) layoutView.findViewById(R.id.done_product);
        
        quantitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quantityLabel.setText(Integer.toString(progress) + " g");
                updateNutritionValues(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFoodAndReset();
            }
        });

        initData();
        
        return layoutView;
    }

    private void saveFoodAndReset() {
        if (isEditable) {
            saveFood();
        }
        
        ((MainActivity) getActivity()).resetFragments();
    }

    private void saveFood() {
        // First, save the new weight
        float w = Float.parseFloat(food.getDataSet().getValue(food.getMealTitle(), "weight"));
        float factor = Float.parseFloat(Integer.toString(quantitySeekbar.getProgress())) / w;

        food.setFactor(factor);

        LocalStorage localStorage = new LocalStorage(LocalStorage.STORAGE_KEY);

        ArrayList<Food> foods = (ArrayList<Food>) localStorage.retrieveObject(Constants.FOOD_ARRAYLIST_CACHE);

        if (foods == null) {
            foods = new ArrayList<>();
        }

        foods.add(food);

        localStorage.serializeAndStore(Constants.FOOD_ARRAYLIST_CACHE, foods);
        ((MainActivity) getActivity()).resetFragments();
    }

    private void updateNutritionValues(int newWeight) {
        if (!nutritionTextViewList.isEmpty() && !nutritionValueList.isEmpty()) {
            for (int i = 0; i < nutritionTextViewList.size(); i++) {
                float value = nutritionValueList.get(i);
                TextView tv = nutritionTextViewList.get(i);

                float newValue = value * (((float) newWeight) / foodWeight);
                tv.setText(Float.toString(newValue));
            }
        }
    }

    private void initData() {
        foodWeight = Integer.parseInt(food.getDataSet().getValue(food.getMealTitle(), "weight"));

        quantityLabel.setText("" + foodWeight);
        quantitySeekbar.setMax((int) Math.floor(foodWeight * 2));
        quantitySeekbar.setProgress(foodWeight);
        quantitySeekbar.setEnabled(isEditable);

        foodTitleTextView.setText(food.getMealTitle().substring(0, 1).toUpperCase() + food.getMealTitle().substring(1));
        generateNutritionViews();
    }

    private void generateNutritionViews() {
        for (String attrName : Utility.getFormattedAttributeNames(food.getKey())) {
            if ((!attrName.contains("FOOD NAME")) && (!attrName.contains("WEIGHT"))) {
                String attrValue;

                if (attrName.contains(" (")) {
                    attrValue = food.getDataSet().getValue(food.getMealTitle(), attrName.substring(0, attrName.indexOf(" (")).toLowerCase());
                } else {
                    attrValue = food.getDataSet().getValue(food.getMealTitle(), attrName.toLowerCase());
                }

                // If the attribute is empty, don't bother showing it
                if (!attrValue.equalsIgnoreCase("")) {
                    generateNutritionTextView(attrName, attrValue);
                }
            }
        }
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
        attributeValueTextView.setTextColor(context.getResources().getColor(R.color.darker_grey));
        attributeValueTextView.setLayoutParams(lpValue);

        nutritionLayout.addView(attributeLabelTextView);
        nutritionLayout.addView(attributeValueTextView);
        nutritionTextViewList.add(attributeValueTextView);

        // Keep track of the nutrition values in order
        try {
            nutritionValueList.add(NumberFormat.getInstance().parse(attrValue.replaceAll("[a-zA-Z]", "")).floatValue());
        } catch (ParseException e) {
            nutritionValueList.add((float) 0.0);
        }
    }

    public void setData(Food food, boolean isEditable) {
        this.food = food;
        this.isEditable = isEditable;
    }
}
