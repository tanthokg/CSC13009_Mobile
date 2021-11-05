package com.example.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private final Context context;
    private final int[] images;

    public GalleryAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageItem.setImageResource(images[position]);
        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLargeGalleryItem(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    private void showLargeGalleryItem(int position) {
        Intent intent = new Intent(context, LargeImage.class);
        intent.putExtra("imageResId", images[position]);
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem =itemView.findViewById(R.id.galleryItem);
        }
    }
}
