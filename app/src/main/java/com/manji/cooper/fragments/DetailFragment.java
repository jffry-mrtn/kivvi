package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.manji.cooper.MainActivity;
import com.manji.cooper.R;
import com.manji.cooper.adapter.DetailAdapter;

import java.util.ArrayList;

public class DetailFragment extends Fragment {

    private Context context;
    private View layoutView;

    private ListView nutritionDetailListView;
    private ArrayList<TextView> nutritionTextViewList;
    private ArrayList<String> details;
    private Button doneButton;
    private TextView detailTitleTextView;
    private String measurement;

    public DetailFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_detail, container, false);
        context = getActivity().getApplicationContext();

        nutritionTextViewList = new ArrayList<>();
        detailTitleTextView = (TextView) layoutView.findViewById(R.id.detail_title);
        nutritionDetailListView = (ListView) layoutView.findViewById(R.id.nutrition_details_listview);
        doneButton = (Button) layoutView.findViewById(R.id.done_detail);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });

        initNutritionViews();
        return layoutView;
    }

    private void initNutritionViews() {
        DetailAdapter detailAdapter = new DetailAdapter(context, details);
        nutritionDetailListView.setAdapter(detailAdapter);

        detailTitleTextView.setText(detailTitleTextView.getText() + " " + measurement);
    }

    public void setData(String measurement, ArrayList<String> details) {
        this.measurement = measurement;
        this.details = details;
    }
}
