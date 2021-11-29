package com.example.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Locale;

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder> {
    private final Context context;
    private String pathToPicturesFolder;
    private File pictureFile;
    private File[] pictureFiles;
    private SparseBooleanArray mSelectedItemsIds;

    private ArrayList<String> paths;

    public PicturesAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictures_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get item path at current position
        String picturePath = paths.get(position);
        // Set item to the ImageView using Glide library
        // holder.imageItem.setImageDrawable(Drawable.createFromPath(picturePath));
        Glide.with(context).asBitmap().load(picturePath).into(holder.imageItem);

        if(mSelectedItemsIds.get(position))
        {
            holder.itemView.setBackgroundColor( 0x9934B5E4);
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(true);
        }
        else
        {
            holder.checkbox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageItem;
        private CheckBox checkbox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.picturesItem);
            checkbox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void toggleSelection(int position) {
        if (mSelectedItemsIds.get(position)) {
            mSelectedItemsIds.delete(position);
        } else {
            mSelectedItemsIds.put(position, true);
        }
        notifyItemChanged(position);
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds.clear();
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
