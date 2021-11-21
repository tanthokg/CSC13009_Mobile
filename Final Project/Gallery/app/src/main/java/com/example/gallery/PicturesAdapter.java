package com.example.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder> {
    private final Context context;
    private final String pathToPicturesFolder;
    private final File pictureFile;
    private File[] pictureFiles;

    public PicturesAdapter(Context context, String pathToPicturesFolder) {
        this.context = context;
        this.pathToPicturesFolder = pathToPicturesFolder;
        this.pictureFile = new File(pathToPicturesFolder);

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.toLowerCase().endsWith("png") || s.toLowerCase(Locale.ROOT).endsWith("jpg");
            }
        };
        this.pictureFiles = pictureFile.listFiles(filter);
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
        String picturePath = pictureFiles[position].getAbsolutePath();
        // Set item to the ImageView using Glide library
        // holder.imageItem.setImageDrawable(Drawable.createFromPath(picturePath));
        Glide.with(context).asBitmap().load(picturePath).into(holder.imageItem);

        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send the folder path and the current position to a new activity
                // Sending the string was more preferable. If you want to send a File object
                // you can use GSON library to serialize and deserialize objects
                showLargeGalleryItem(pathToPicturesFolder, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureFiles.length;
    }

    private void showLargeGalleryItem(String pathToPicturesFolder, int itemPosition) {
        Intent intent = new Intent(context, LargeImage.class);
        // Send the folder path and the current position to the destination activity
        intent.putExtra("pathToPicturesFolder", pathToPicturesFolder);
        intent.putExtra("itemPosition", itemPosition);
        context.startActivity(intent);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.picturesItem);
        }
    }
}
