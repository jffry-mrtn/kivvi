package com.manji.cooper.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.ListView;

import com.manji.cooper.R;
import com.manji.cooper.model.Constants;

public class ProductFragment extends Fragment {

    private Context context;
    private View layoutView;
    private ActionMode actionMode;

    private Button saveProductButton;
    private Button cancelButton;
    private ListView productListView;
    private EditText enterProductEditText;

    public ProductFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_product, container, false);
        context = getActivity().getApplicationContext();

        enterProductEditText = (EditText) layoutView.findViewById(R.id.enter_product_edittext);
        productListView = (ListView) layoutView.findViewById(R.id.product_listview);
        saveProductButton = (Button) layoutView.findViewById(R.id.save_product_button);
        cancelButton = (Button) layoutView.findViewById(R.id.cancel_button);
        
        saveProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment();
            }
        });

        return layoutView;
    }

    private void popFragment() {
        getActivity().onBackPressed();
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
            final int numSelected = productListView.getCheckedItemCount();

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
