package com.example.gallery;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder> {
    private final Context context;
    private SparseBooleanArray selectedItemsIds;

    private ArrayList<String> paths;
    private final int spanCount;

    public PicturesAdapter(Context context, ArrayList<String> paths, int spanCount) {
        this.context = context;
        this.paths = paths;
        selectedItemsIds = new SparseBooleanArray();
        this.spanCount = spanCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (spanCount != 1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictures_item, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictures_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get item path at current position
        String picturePath = paths.get(position);
        // Set item to the ImageView using Glide library
        // holder.imageItem.setImageDrawable(Drawable.createFromPath(picturePath));
        Glide.with(context).asBitmap().load(picturePath).into(holder.imageItem);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        // Set width and height of ImageView
        ((MainActivity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        // Depend on how many columns of images are displayed in view
        if (spanCount != 1) {
            int size = displaymetrics.widthPixels / spanCount;
            holder.imageItem.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
        }
        else {
            // Set image size to display
            int size = displaymetrics.widthPixels / 4;
            holder.imageItem.setLayoutParams(new RelativeLayout.LayoutParams(size, size));

            // Set the information of image
            File pictureFile = new File(paths.get(position));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);

            int lastSlash = pictureFile.getAbsolutePath().lastIndexOf('/');
            String imageName = pictureFile.getAbsolutePath().substring(lastSlash + 1);
            holder.txtNameImage.setText(imageName);
            holder.txtSizeAndDateImage.setText(Math.round(pictureFile.length() * 1.0 / 1000) + " KB");
            holder.txtSizeAndDateImage.append(", ");
            holder.txtSizeAndDateImage.append(sdf.format(pictureFile.lastModified()));
        }


        if(selectedItemsIds.get(position))
        {
            holder.itemView.setBackgroundColor(0x9934B5E4);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageItem;
        private CheckBox checkbox;
        private TextView txtNameImage;
        private TextView txtSizeAndDateImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (spanCount != 1) {
                imageItem = itemView.findViewById(R.id.picturesItem);
                checkbox = itemView.findViewById(R.id.checkBox);
            }
            else {
                imageItem = itemView.findViewById(R.id.picturesListItem);
                checkbox = itemView.findViewById(R.id.checkListBox);
                txtNameImage = itemView.findViewById(R.id.txtNameImage);
                txtSizeAndDateImage = itemView.findViewById(R.id.txtSizeAndDateImage);
            }
        }
    }

    public void toggleSelection(int position) {
        if (selectedItemsIds.get(position)) {
            selectedItemsIds.delete(position);
        } else {
            selectedItemsIds.put(position, true);
        }
        notifyItemChanged(position);
    }

    //Remove selected selections
    public void removeSelection() {
        selectedItemsIds.clear();
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return selectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return selectedItemsIds;
    }

    //Select all
    public void selectAll() {
        selectedItemsIds.clear();

        for (int i = 0; i < paths.size(); i++) {
            selectedItemsIds.put(i, true);
            notifyItemChanged(i);
        }
        //mSelectedItemsIds.size(paths.size());
        notifyDataSetChanged();
    }
}
