package com.manji.cooper.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.components.Legend;
import com.manji.cooper.MainActivity;
import com.manji.cooper.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.managers.DataManager;
import com.manji.cooper.model.Constants;
import com.manji.cooper.model.Food;
import com.manji.cooper.utils.LocalStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class MainFragment extends Fragment {

    private Context context;
    private View layoutView;
    private ActionMode actionMode;

    private ListView historyListView;
    private ImageButton barcodeButton;
    private Button enterProductButton;
    private TextView caloriesTextView;
    private ArrayList<Food> foods;

    private PieChart graph;

    public MainFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_main, container, false);
        context = getActivity().getApplicationContext();

        //caloriesTextView = (TextView) layoutView.findViewById(R.id.dashboard_calories);

        barcodeButton = (ImageButton) layoutView.findViewById(R.id.scan_barcode_button);
        enterProductButton = (Button) layoutView.findViewById(R.id.enter_product_button);
        historyListView = (ListView) layoutView.findViewById(R.id.history_listview);

        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamera();
            }
        });

        enterProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterProduct();
            }
        });

        initGraph();

        return layoutView;
    }

    private void initGraph(){
        graph = (PieChart)layoutView.findViewById(R.id.pc_chart);

        graph.setUsePercentValues(true);
        graph.setDescription("");
//
//        graph.setRotationEnabled(false);

    }

    private void setGraphData(){
        if (foods == null || foods.size() == 0) return;

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        HashMap<String, ItemInfo> items = DataManager.getInstance().getItems();
        HashMap<String, Float> totals = new HashMap<>();

        //the relevant attributes
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("protein");
        xVals.add("carbohydrate");
        xVals.add("total sugar");
        xVals.add("total fat");
        xVals.add("cholesterol");
        xVals.add("total dietary fibre");
        xVals.add("saturated fat");

        //add totals of all relevant attributes into table
        for (Food f: foods) {
            for (String v: xVals){
                int index = f.getDataSet().getAttributeNames().indexOf(v);

                if (totals.containsKey(v)){
                    try{
                        totals.put(v, Float.parseFloat(items.get(f.getMealTitle()).values.get(index)) + totals.get(v));
                    }catch (Exception ex){
                        totals.put(v, 0.0f + totals.get(v));
                    }
                }else{
                    try{
                        totals.put(v, Float.parseFloat(items.get(f.getMealTitle()).values.get(index)));
                    }catch (Exception ex){
                        totals.put(v, 0.0f);
                    }
                }
            }
        }

        int index = 0;
        for (String t: totals.keySet()){
            //Only include non-zero totals in graph
            if (totals.get(t) == 0){
                xVals.remove(t);
            }else{
                yVals.add(new Entry(totals.get(t), index));
                index++;
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals, "");

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        graph.animateY(800);
        graph.setVisibility(View.VISIBLE);
        graph.setDrawSliceText(false);

        PieData pieData = new PieData(xVals, dataSet);
        graph.setData(pieData);

        Legend leg = graph.getLegend();
        leg.setPosition(Legend.LegendPosition.PIECHART_CENTER);

        //graph.invalidate();
    }

    private void showCamera() {
        ((MainActivity) getActivity()).showScannerFragment();
    }

    private void showEnterProduct() {
        ((MainActivity) getActivity()).showProductFragment();
    }

    @Override
    public void onResume() {
        LocalStorage localStorage = new LocalStorage(LocalStorage.STORAGE_KEY);
        foods = (ArrayList<Food>) localStorage.retrieveObject(Constants.FOOD_ARRAYLIST_CACHE);

        if (foods == null) {
            foods = new ArrayList<>();
        }

        setGraphData();

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /** Search **/
    public void search(CharSequence query) {
        if (query.length() > 0) {
//            mainAdapter.getFilter().filter(query);
        }
    }

    public void clearSearchFilter() {
//        mainAdapter.getFilter().filter("");

//        mainAdapter = new MainAdapter(context);
//        mainListView.setAdapter(mainAdapter);
//        mainAdapter.notifyDataSetChanged();
    }

    public void clearItemSelection() {
//        mainAdapter.notifyDataSetChanged();
    }

    private class ActionModeCallback implements ListView.MultiChoiceModeListener {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.main_context_menu, menu);
            mode.setTitle(getResources().getString(R.string.select_elements));
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            actionMode = mode;
            switch (item.getItemId()) {
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
            final int numSelected = historyListView.getCheckedItemCount();

            switch (numSelected) {
                case 0:
                    actionMode.setSubtitle(null);
                    break;
                case 1:
                    actionMode.setSubtitle(getResources().getString(R.string.one_item_selected));
                    break;
                default:
                    actionMode.setSubtitle(String.format(getResources().getString(R.string.more_items_selected), numSelected));
                    break;
            }
        }
    };

}
