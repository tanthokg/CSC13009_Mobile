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
    private final Context context;
    private final PicturesAdapter picturesAdapter;

    public ToolbarActionModeCallback(Context context, PicturesAdapter picturesAdapter) {
        this.context = context;
        this.picturesAdapter = picturesAdapter;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.toolbar_multiple_items, menu);
        return true;
    }
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.recoverMulti).setVisible(false);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getItemId() == R.id.delete)
        {
            // Delete Multiple Images
            ((MainActivity)context).picturesFragment.deleteMulti();
        }
        else if (item.getItemId() == R.id.share)
        {
            // Share Multiple Images
            ((MainActivity)context).picturesFragment.shareMulti();
        }
        else if (item.getItemId() == R.id.selectAll)
        {
            Toast.makeText(context.getApplicationContext(), "Select all", Toast.LENGTH_SHORT).show();
            ((MainActivity)context).picturesFragment.selectAll();
        }
        else if (item.getItemId() == R.id.addToAlbum)
        {
            ((MainActivity)context).picturesFragment.addToAlbum();
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        picturesAdapter.removeSelection();
        ((MainActivity)context).picturesFragment.setNullToActionMode();
        ((MainActivity)context).bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
