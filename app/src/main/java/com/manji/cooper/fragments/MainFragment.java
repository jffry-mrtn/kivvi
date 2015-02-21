package com.manji.cooper.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
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

import com.manji.cooper.R;
import com.manji.cooper.model.Constants;

public class MainFragment extends Fragment {

    private Context context;
    private View layoutView;
    private ActionMode actionMode;

    private ListView historyListView;
    private ImageButton barcodeButton;
    private Button enterProductButton;
    private TextView caloriesTextView;


    public MainFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_main, container, false);
        context = getActivity().getApplicationContext();

        caloriesTextView = (TextView) layoutView.findViewById(R.id.dashboard_calories);
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

        return layoutView;
    }

    private void showCamera() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction()
                .replace(R.id.frame, new ScannerFragment(), Constants.SCANNER_FRAGMENT_TAG);

        fragmentTransaction.commit();
    }

    private void showEnterProduct() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction()
                .replace(R.id.frame, new ProductFragment(), Constants.PRODUCT_FRAGMENT_TAG);

        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
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
