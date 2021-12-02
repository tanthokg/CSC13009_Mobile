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

public class PicturesListAdapter extends RecyclerView.Adapter<PicturesListAdapter.ViewHolder> {

    private final Context context;
    private SparseBooleanArray mSelectedItemsIds;

    private ArrayList<String> paths;

    public PicturesListAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictures_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get item path at current position
        String picturePath = paths.get(position);
        // Set item to the ImageView using Glide library
        // holder.imageItem.setImageDrawable(Drawable.createFromPath(picturePath));
        Glide.with(context).asBitmap().load(picturePath).into(holder.imageListItem);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        // Set width and height of ImageView
        ((MainActivity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        // Set image size to display
        int size = displaymetrics.widthPixels / 4;
        holder.imageListItem.setLayoutParams(new RelativeLayout.LayoutParams(size, size));

        // Set the information of image
        File pictureFile = new File(paths.get(position));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);

        int lastSlash = pictureFile.getAbsolutePath().lastIndexOf('/');
        String imageName = pictureFile.getAbsolutePath().substring(lastSlash + 1);
        holder.txtNameImage.setText(imageName);
        holder.txtSizeAndDateImage.setText(Math.round(pictureFile.length() * 1.0 / 1000) + " KB");
        holder.txtSizeAndDateImage.append(", ");
        holder.txtSizeAndDateImage.append(sdf.format(pictureFile.lastModified()));


        if(mSelectedItemsIds.get(position))
        {
            holder.itemView.setBackgroundColor( 0x9934B5E4);
            holder.checkListbox.setVisibility(View.VISIBLE);
            holder.checkListbox.setChecked(true);
        }
        else
        {
            holder.checkListbox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageListItem;
        private CheckBox checkListbox;
        private TextView txtNameImage;
        private TextView txtSizeAndDateImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageListItem = itemView.findViewById(R.id.picturesListItem);
            checkListbox = itemView.findViewById(R.id.checkListBox);
            txtNameImage = itemView.findViewById(R.id.txtNameImage);
            txtSizeAndDateImage = itemView.findViewById(R.id.txtSizeAndDateImage);
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
