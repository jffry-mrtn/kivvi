package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.manji.cooper.MainActivity;
import com.manji.cooper.R;
import com.manji.cooper.adapter.DetailAdapter;
import com.manji.cooper.adapter.HistoryAdapter;
import com.manji.cooper.model.Constants;
import com.manji.cooper.model.Food;
import com.manji.cooper.utils.LocalStorage;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private Context context;
    private View layoutView;

    private ListView historyListView;
    private HistoryAdapter historyAdapter;
    ArrayList<Food> foodHistory;

    public HistoryFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_history, container, false);
        context = getActivity().getApplicationContext();

        historyListView = (ListView) layoutView.findViewById(R.id.history_listview);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Open NutritionFragment from here or MainActivity
                ((MainActivity) getActivity()).showNutritionFragment(historyAdapter.getItem(position), false);
            }
        });

        initHistory();
        return layoutView;
    }

    private void initHistory() {
        LocalStorage localStorage = new LocalStorage(LocalStorage.STORAGE_KEY);
        foodHistory = (ArrayList<Food>) localStorage.retrieveObject(Constants.FOOD_ARRAYLIST_CACHE);

        historyAdapter = new HistoryAdapter(context, foodHistory);
        historyListView.setAdapter(historyAdapter);
    }
}
