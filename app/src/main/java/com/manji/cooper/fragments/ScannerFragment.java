package com.manji.cooper.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manji.cooper.R;
import com.manji.cooper.model.Constants;
import com.manji.cooper.utils.Utility;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by douglaspereira on 2015-02-12.
 */
public class ScannerFragment extends Fragment implements ZBarScannerView.ResultHandler{
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private ZBarScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;

    private View layoutView;
    private Context context;

    public ScannerFragment()
    {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        init();
        return mScannerView;
    }

    protected void init () {
        mScannerView = new ZBarScannerView(Utility.activity);

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

    @Override
    public void handleResult(Result rawResult) {
        Log.d ("Scanner", "Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName());

        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, new ProductFragment(), Constants.SCANNER_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(Constants.SCANNER_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

}
