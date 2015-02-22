package com.manji.cooper.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.managers.DataManager;
import com.manji.cooper.model.Constants;
import com.manji.cooper.model.Food;
import com.manji.cooper.utils.LocalStorage;
import com.manji.cooper.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment implements OnChartValueSelectedListener{

    private final String TAG = MainFragment.class.getSimpleName();

    private Context context;
    private View layoutView;
    private ActionMode actionMode;

    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fabScanBarcode;
    private FloatingActionButton fabEnterProduct;

    private ArrayList<Food> foods;

    HashMap<String, GraphItemDetails> graphInfo;
    private PieChart graph;

    private Handler hideDetailsHandler;
    private  View selected_item_cont;
    private TextView tv_details;
    private Button bt_more;

    private String currentSelectedItem = "";

    private class GraphItemDetails {
        public float total;
        public String label;
        public String unit;
        public ArrayList<String> details;
        public int fid;
        public ArrayList<Food> items;
        public int xIndex;

        public GraphItemDetails(){
            total = 0.0f;
            xIndex = 0;
            label = "";
            unit = "";
            details = new ArrayList<>();
            fid = 0;
            items = new ArrayList<>();
        }
    }

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

        tv_details = (TextView)layoutView.findViewById(R.id.tv_selected_item_detail);
        selected_item_cont = layoutView.findViewById(R.id.selected_item_detail_cont);
        bt_more = (Button)layoutView.findViewById(R.id.bt_more_info);

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

        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, graphInfo.get(currentSelectedItem).details.toString());

            }
        });

        initGraph();

        return layoutView;
    }

    private void initGraph(){
        graph = (PieChart)layoutView.findViewById(R.id.pc_chart);

        graph.setDescription("");
        graph.setOnChartValueSelectedListener(this);
//
//        graph.setRotationEnabled(false);

    }

    private void setGraphData(){
        if (foods == null || foods.size() == 0) return;

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        HashMap<String, ItemInfo> items = DataManager.getInstance().getItems();

        graphInfo = new HashMap<>();

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

                if (graphInfo.containsKey(v)){
                    GraphItemDetails d = graphInfo.get(v);
                    d.items.add(f);

                    try{
                        d.total += Float.parseFloat(items.get(f.getMealTitle()).values.get(index)) * f.getFactor();
                    }catch (Exception ex) {
                        Log.d(TAG, ex.toString());
                    }finally {
                        graphInfo.put(v, d);
                    }

                }else{
                    GraphItemDetails d = new GraphItemDetails();
                    d.items.add(f);
                    d.label = v;
                    d.fid = f.getFid();

                    CSVData data = f.getDataSet();

                    List<String> attributes = data.getAttributeNames();
                    int u = attributes.indexOf(v);

                    d.unit = (u < 0 || u >= data.getAttributeUnits().size()) ? "" : data.getAttributeUnits().get(u);

                    try{
                        d.total = Float.parseFloat(items.get(f.getMealTitle()).values.get(index)) * f.getFactor();
                    }catch (Exception ex) {
                        Log.d(TAG, ex.toString());
                    }finally {
                        graphInfo.put(v, d);
                    }
                }
            }
        }

        int index = 0;
        for (String t: graphInfo.keySet()){
            //Only include non-zero totals in graph
            if (graphInfo.get(t).total == 0){
                xVals.remove(t);
            }else{
                graphInfo.get(t).xIndex = index;
                yVals.add(new Entry(graphInfo.get(t).total, index));
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

        graph.animateY(600);
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
    public void onValueSelected(Entry entry, int i) {
        if (graphInfo == null) return;
        String res = "";

        ArrayList<String> details = new ArrayList<>();

        for (String g: graphInfo.keySet()){
            if (graphInfo.get(g).xIndex == entry.getXIndex()){
                currentSelectedItem = g;

                res = String.format("%.01f%s from %d items\n", entry.getVal(), graphInfo.get(g).unit, graphInfo.get(g).items.size());

                for (Food f: graphInfo.get(g).items) {
                    CSVData data = f.getDataSet();

                    List<String> attributes = data.getAttributeNames();
                    int index = attributes.indexOf(graphInfo.get(g).label);

                    float weight = 0.0f;

                    try {
                        weight = f.getFactor() * Float.parseFloat(DataManager.getInstance().getItems().get(f.getMealTitle()).values.get(index));
                    } catch (Exception ex) {
                        Log.e(TAG, ex.toString());
                    }

                    details.add(String.format("%.01f%s of '%s'\n", weight, graphInfo.get(g).unit, f.getMealTitle()));
                }

                graphInfo.get(g).details = details;
                break;

            }
        }

        tv_details.setText(res);
        selected_item_cont.setVisibility(View.VISIBLE);

        hideDetailsHandler = new Handler();

        hideDetailsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selected_item_cont.setVisibility(View.GONE);
                hideDetailsHandler.removeCallbacks(this);
            }
        }, 3500);
    }

    @Override
    public void onNothingSelected() {
        Log.d(TAG, "Nothing");
    }
}
