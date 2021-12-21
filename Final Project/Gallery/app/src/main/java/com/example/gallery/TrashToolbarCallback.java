package com.example.gallery;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;

public class TrashToolbarCallback implements ActionMode.Callback {
    private final Context context;
    private final PicturesAdapter picturesAdapter;

    public TrashToolbarCallback(Context context, PicturesAdapter picturesAdapter) {
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
        menu.findItem(R.id.share).setVisible(false);
        menu.findItem(R.id.addToAlbum).setVisible(false);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();
        if (R.id.delete == id) {
            ((MainActivity)context).trashedFragment.deleteMulti();
        } else if (R.id.recoverMulti == id)
        {
            ((MainActivity)context).trashedFragment.recoverMulti();
        } else if (R.id.selectAll == id)
        {
            ((MainActivity)context).trashedFragment.selectAll();
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        picturesAdapter.removeSelection();
        ((MainActivity)context).trashedFragment.setNullToActionMode();
        ((MainActivity)context).bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
