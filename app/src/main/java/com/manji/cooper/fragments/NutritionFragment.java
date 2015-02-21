package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    public NutritionFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_nutrition, container, false);
        context = getActivity().getApplicationContext();

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

        return layoutView;
    }
}
