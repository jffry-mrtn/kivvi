package com.manji.cooper.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.managers.DataManager;
import com.manji.cooper.model.Constants;
import com.manji.cooper.model.Food;
import com.manji.cooper.utils.LocalStorage;
import com.manji.cooper.utils.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private  View chartDetailView;
    private TextView tv_details;
    private Button bt_more;
    private TextView homeTitleTextView;

    private String currentSelectedItem = "";
    private View titleLayout;
    private View time_frame_cont;

    private View bt_day, bt_week, bt_month;

    private boolean detailsVisible = false;

    private enum TIME_FRAME{
        DAY, WEEK, MONTH;
    }

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
        chartDetailView = layoutView.findViewById(R.id.selected_item_detail_cont);
        bt_more = (Button)layoutView.findViewById(R.id.bt_more_info);
        homeTitleTextView = (TextView) layoutView.findViewById(R.id.home_title);
        titleLayout = layoutView.findViewById(R.id.title_layout);
        time_frame_cont = layoutView.findViewById(R.id.time_frame_filter_cont);

        bt_day = layoutView.findViewById(R.id.day_toggle);
        bt_week = layoutView.findViewById(R.id.week_toggle);
        bt_month = layoutView.findViewById(R.id.month_toggle);

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
                ((MainActivity) getActivity()).showDetailFragment(currentSelectedItem, graphInfo.get(currentSelectedItem).details);
            }
        });

        bt_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGraphData(TIME_FRAME.DAY);

            }
        });
        bt_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGraphData(TIME_FRAME.WEEK);

            }
        });
        bt_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGraphData(TIME_FRAME.MONTH);

            }
        });

        initGraph();

        return layoutView;
    }

    private void initGraph(){
        graph = (PieChart)layoutView.findViewById(R.id.pc_chart);

        graph.setDescription("");
        graph.setOnChartValueSelectedListener(this);

    }

    private void setGraphData(TIME_FRAME timeFrame){
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c = clearCalendar(c);

        Date timeStart = null, timeEnd = null;

        c.add(Calendar.DATE, -1);
        timeStart = c.getTime();

        c.add(Calendar.DATE, 2);
        timeEnd = c.getTime();

        homeTitleTextView.setText(getResources().getString(R.string.overview_day));

        if (timeFrame.equals(TIME_FRAME.WEEK)){
            c = clearCalendar(c);
            c.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_WEEK));
            timeStart = c.getTime();
            c.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_WEEK));
            timeEnd = c.getTime();

            homeTitleTextView.setText(getResources().getString(R.string.overview_week));


        }else if (timeFrame.equals(TIME_FRAME.MONTH)){
            c = clearCalendar(c);
            c.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
            timeStart = c.getTime();
            c.set(Calendar.DATE, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
            timeEnd = c.getTime();

            homeTitleTextView.setText(getResources().getString(R.string.overview_month));


        }

        System.out.println(timeEnd + " - " + timeStart);

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        if (foods == null || foods.size() == 0) {

            yVals.add(new Entry(1.0f, 0));
            xVals.add(getResources().getString(R.string.empty_chart_text));

            graph.setUsePercentValues(true);
            homeTitleTextView.setText(getResources().getString(R.string.no_data_intructional_copy));
            homeTitleTextView.setVisibility(View.VISIBLE);
            homeTitleTextView.setTextSize(20);
            homeTitleTextView.setAllCaps(false);
            time_frame_cont.setVisibility(View.GONE);

        } else {
            graph.setUsePercentValues(false);
            homeTitleTextView.setVisibility(View.VISIBLE);
            homeTitleTextView.setTextSize(18);

            time_frame_cont.setVisibility(View.VISIBLE);

            HashMap<String, ItemInfo> items = DataManager.getInstance().getItems();

            graphInfo = new HashMap<>();

            //the relevant attributes

            xVals.add("protein");
            xVals.add("carbohydrate");
            xVals.add("total sugar");
            xVals.add("total fat");
            xVals.add("cholesterol");
            xVals.add("total dietary fibre");
            xVals.add("saturated fat");

            //add totals of all relevant attributes into table
            for (Food f: foods) {
                if (f.getTimestamp().after(timeStart) && f.getTimestamp().before(timeEnd)){
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
                }else {
                    return;
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
        }

        PieDataSet dataSet = new PieDataSet(yVals, "");

        // add a lot of colors
        dataSet.setColors(new int[] {
                getResources().getColor(R.color.graph_7),
                getResources().getColor(R.color.graph_6),
                getResources().getColor(R.color.graph_5),
                getResources().getColor(R.color.graph_4),
                getResources().getColor(R.color.graph_3),
                getResources().getColor(R.color.graph_2),
                getResources().getColor(R.color.graph_1) });

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

    private Calendar clearCalendar(Calendar c){
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.clear(Calendar.MINUTE);
        c.clear(Calendar.SECOND);
        c.clear(Calendar.MILLISECOND);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());

        return c;
    }

    @Override
    public void onResume() {
        LocalStorage localStorage = new LocalStorage(LocalStorage.STORAGE_KEY);
        foods = (ArrayList<Food>) localStorage.retrieveObject(Constants.FOOD_ARRAYLIST_CACHE);

        if (foods == null) {
            foods = new ArrayList<>();
        }

        setGraphData(TIME_FRAME.DAY);

        super.onResume();
    }

    @Override
    public void onValueSelected(Entry entry, int i) {
        if (graphInfo == null || foods == null || foods.size() == 0) return;

        String res = "";

        ArrayList<String> details = new ArrayList<>();

        for (String g: graphInfo.keySet()){
            if (graphInfo.get(g).xIndex == entry.getXIndex()){
                currentSelectedItem = g;

                res = String.format("%.01f %s of %s from %d item(s)\n",
                        entry.getVal(), graphInfo.get(g).unit, g, graphInfo.get(g).items.size());

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

        if(!detailsVisible){
            chartDetailView.setVisibility(View.VISIBLE);
            chartDetailView.startAnimation( AnimationUtils.loadAnimation(Utility.activity, R.anim.slide_down));
            detailsVisible = true;
        }

    }

    @Override
    public void onNothingSelected() {
        chartDetailView.setVisibility(View.GONE);
        chartDetailView.startAnimation(AnimationUtils.loadAnimation(Utility.activity, R.anim.slide_up));

        detailsVisible = false;
    }

    public void redrawGraph(){
        if (graph == null) return;

        setGraphData(TIME_FRAME.DAY);
    }
}
