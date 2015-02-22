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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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

    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fabScanBarcode;
    private FloatingActionButton fabEnterProduct;

    private ArrayList<Food> foods;

    private PieChart graph;

    public MainFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_main, container, false);
        context = getActivity().getApplicationContext();

        fabMenu = (FloatingActionsMenu) layoutView.findViewById(R.id.fab);
        fabScanBarcode = (FloatingActionButton) layoutView.findViewById(R.id.scan_barcode);
        fabEnterProduct = (FloatingActionButton) layoutView.findViewById(R.id.enter_product);

        fabScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCamera();
            }
        });

        fabEnterProduct.setOnClickListener(new View.OnClickListener() {
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
        dataSet.setColors(new int[] {
                getResources().getColor(R.color.graph_1),
                getResources().getColor(R.color.graph_2),
                getResources().getColor(R.color.graph_3),
                getResources().getColor(R.color.graph_4),
                getResources().getColor(R.color.graph_5),
                getResources().getColor(R.color.graph_6),
                getResources().getColor(R.color.graph_7) });

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
}
