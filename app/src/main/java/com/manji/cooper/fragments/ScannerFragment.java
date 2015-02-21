package com.manji.cooper.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manji.cooper.R;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.custom.Resource;
import com.manji.cooper.managers.DataManager;
import com.manji.cooper.managers.ResourceHandler;
import com.manji.cooper.model.Constants;
import com.manji.cooper.utils.Utility;

import org.json.JSONObject;
import android.os.Handler;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by douglaspereira on 2015-02-12.
 */
public class ScannerFragment extends Fragment implements ZBarScannerView.ResultHandler{

    private final String TAG = ScannerFragment.class.getSimpleName();

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private ZBarScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;

    private View layoutView;
    private Context context;

    private TextView itemDetailText;

    public ScannerFragment()
    {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.scanner_fragment, container, false);
        context = getActivity().getApplicationContext();
        init();

        return layoutView;
    }

    protected void init () {

        mScannerView = (ZBarScannerView)layoutView.findViewById(R.id.zb_scanner);
        itemDetailText = (TextView)layoutView.findViewById(R.id.tv_item_detail);

    }


    @Override
    public void onResume() {
        super.onResume();

        startScanner();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mScannerView == null) return;

        stopScanner();
    }

    public void stopScanner(){
        if (mScannerView == null) return;

        mScannerView.stopCamera();
        mScannerView.setResultHandler(null);
    }

    public void startScanner(){
        if (mScannerView == null){
            mScannerView = new ZBarScannerView(Utility.activity);
        }
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    /* Generate list of 1-2 word queries based on item name
     *
     */
    private ArrayList<String> generateQueries(String itemName) {
        List<String> words = Arrays.asList(itemName.split("\\s+"));
        ArrayList<String> queries = new ArrayList<>();

        for (int queryLength = 1; queryLength <= 2; queryLength++){
            for (int w=0; w < words.size(); w++){

                if (w + queryLength > words.size())
                    break;

                String q = TextUtils.join(" ", words.subList(w, w + queryLength));
                queries.add(q);
            }
        }

        return queries;
    }

    /* Filter data set results based on generated queries
     *
     */
    private HashMap<String, ItemInfo> getFilteredResults(String itemName){

        ArrayList<String> queries = generateQueries(itemName);
        HashMap<String, ItemInfo> filteredResults = new HashMap<>();

        for (String q: queries){
            HashMap<String, ItemInfo> r = DataManager.getInstance().getFilteredData(q);

            for (String s: r.keySet()){
                filteredResults.put(s, r.get(s));
            }
        }

       return filteredResults;

    }

    private Resource.OnRetrievedListener outpanJsonRetrievedListener = new Resource.OnRetrievedListener() {
        @Override
        public void onRetrieved(Resource resource) {
            Log.d(TAG, resource.getContent());

            try{
                JSONObject jsonObject = new JSONObject(resource.getContent());
                String name = null;
                String code = null;
                String error = null;

                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");

                if (jsonObject.has("barcode"))
                    code = jsonObject.getString("barcode");

                if (jsonObject.has("error"))
                    error = jsonObject.getString("error");

                final boolean itemMatch = (name != null && !name.isEmpty() && error == null);

                if (!itemMatch)
                    itemDetailText.setText("Couldn't recognize barcode");
                else if (name != null && code != null)
                    itemDetailText.setText(name + " " + code);

                final String itemName = name;

                //TEMP... Leave fragment after a few seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopScanner();

                        ProductFragment productFragment = new ProductFragment();

                        if (itemMatch){
                            HashMap<String, ItemInfo> r = getFilteredResults(itemName);
                            productFragment.setFilteredResults(r);
                        }

                        if (isAdded()){
                            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, productFragment, Constants.SCANNER_FRAGMENT_TAG);
                            fragmentTransaction.addToBackStack(Constants.SCANNER_FRAGMENT_TAG);
                            fragmentTransaction.commit();
                        }


                    }
                }, 3000);


            }catch (Exception ex){
                Log.e(TAG, ex.toString());
                itemDetailText.setText("Couldn't recognize barcode");
            }
        }

        @Override
        public void onError(String error) {
            Log.e(TAG, error);
        }
    };

    @Override
    public void handleResult(Result rawResult) {
        //TODO; direct user awys from scanner fragment , probably to item screen

        Log.d (TAG, "Barcode = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName());

        itemDetailText.setText("Checking barcode...");

        String url = Utility.OUTPAN_REQUEST_URL.replace("API_KEY",
                     Utility.activity.getResources().getString(R.string.outpan_api_key))
                     .replace("BAR_CODE", rawResult.getContents());

        ResourceHandler.getInstance().getResource(url, outpanJsonRetrievedListener);
    }

}
