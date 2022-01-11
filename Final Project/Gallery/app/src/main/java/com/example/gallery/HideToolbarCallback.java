package com.example.gallery;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;

public class HideToolbarCallback implements ActionMode.Callback {
    private final Context context;
    private final PicturesAdapter picturesAdapter;

    public HideToolbarCallback(Context context, PicturesAdapter picturesAdapter) {
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
        menu.findItem(R.id.addToAlbum).setVisible(false);
        menu.findItem(R.id.hide).setTitle("Unhide");
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();
        if(item.getItemId() == R.id.delete)
        {
            // Delete Multiple Images
            ((MainActivity)context).hideFragment.deleteMulti();
        }
        else if (item.getItemId() == R.id.hide) {
            //TODO:
            ((MainActivity)context).hideFragment.unhideMulti();
        }
        else if (item.getItemId() == R.id.share)
        {
            // Share Multiple Images
            ((MainActivity)context).hideFragment.shareMulti();
        }
        else if (item.getItemId() == R.id.selectAll)
        {
            ((MainActivity)context).hideFragment.selectAll();
        }

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        picturesAdapter.removeSelection();
        ((MainActivity)context).hideFragment.setNullToActionMode();
        ((MainActivity)context).bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
