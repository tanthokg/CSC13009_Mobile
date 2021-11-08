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

import java.io.File;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private final Context context;
    private final String pathToPicturesFolder;
    private final File pictureFile;
    private final File[] pictureFiles;

    public GalleryAdapter(Context context, String pathToPicturesFolder) {
        this.context = context;
        this.pathToPicturesFolder = pathToPicturesFolder;
        this.pictureFile = new File(pathToPicturesFolder);
        this.pictureFiles = pictureFile.listFiles();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get item path at current position
        String galleryItemPath = pictureFiles[position].getAbsolutePath();
        // Set item to the widget
        holder.imageItem.setImageDrawable(Drawable.createFromPath(galleryItemPath));

        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On click, send the folder path and the current position to the destination activity
                // Sending the string was more preferable, as to send a File object
                // we need to serialize and deserialize the object before we could use it
                showLargeGalleryItem(pathToPicturesFolder, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
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
            imageItem = itemView.findViewById(R.id.galleryItem);
        }
    }
}
