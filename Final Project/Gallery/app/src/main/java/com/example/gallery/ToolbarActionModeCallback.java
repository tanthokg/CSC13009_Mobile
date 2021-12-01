package com.example.gallery;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;

import java.io.File;
import java.util.ArrayList;

public class ToolbarActionModeCallback implements ActionMode.Callback{
    private Context context;
    private PicturesAdapter picturesAdapter;
    private ArrayList<File> message_models;

    public ToolbarActionModeCallback(Context context, PicturesAdapter picturesAdapter, ArrayList<File> message_models) {
        this.context = context;
        this.picturesAdapter = picturesAdapter;
        this.message_models = message_models;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.toolbar_multiple_items, menu);//Inflate the menu over action mode
        return true;
    }
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getItemId() == R.id.delete)
        {
            ((MainActivity)context).picturesFragment.deleteMulti();
            mode.finish();
            ((MainActivity)context).bottomNavigationView.setVisibility(View.VISIBLE);
        }
        else if (item.getItemId() == R.id.share)
        {
            ((MainActivity)context).picturesFragment.shareMulti();
            mode.finish();
            ((MainActivity)context).bottomNavigationView.setVisibility(View.VISIBLE);
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        picturesAdapter.removeSelection();
        ((MainActivity)context).picturesFragment.setNullToActionMode();
        ((MainActivity)context).bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
